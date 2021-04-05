package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.mapper.ReservationMapper;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.model.User;
import ilia.nemankov.repository.ConfigurationRepository;
import ilia.nemankov.repository.ReservationRepository;
import ilia.nemankov.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.*;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final ConfigurationService configurationService;
    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final UserTransaction userTransaction;

    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper, ReservationRepository reservationRepository, ConfigurationService configurationService,
        UserRepository userRepository, ConfigurationRepository configurationRepository, RestTemplateBuilder restTemplateBuilder, UserTransaction userTransaction) {

        this.reservationMapper = reservationMapper;
        this.reservationRepository = reservationRepository;
        this.configurationService = configurationService;
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.restTemplateBuilder = restTemplateBuilder;
        this.userTransaction = userTransaction;

    }

    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Value("${payments.url}")
    private String paymentsURL;

    @Value("${bonuses.percent}")
    private Double bonusesPercent;

    @Override
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        Reservation entity;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName());
        Integer oldUserBonuses = user.getBonuses();

        try {
            userTransaction.begin();


            validate(reservationDTO);

            Reservation reservation = reservationMapper.dtoToEntity(reservationDTO);

            if (!configurationService.isConfigurationAvailable(
                    reservation.getConfiguration(),
                    reservation.getArrivalDate(),
                    reservation.getDepartureDate())
            ) {
                userTransaction.rollback();
                throw new BusyConfigurationException("There isn't any free room with selected configuration");
            }

            int mustPay = configurationRepository.findById(reservation.getConfiguration().getId()).get().getPrice();

            if (reservationDTO.getUseBonuses()) {
                int usedBonuses = mustPay <= user.getBonuses() ? mustPay : user.getBonuses();
                mustPay -= usedBonuses;
                long timeDifference = reservation.getDepartureDate().getTime() - reservation.getArrivalDate().getTime();
                int numberOfDays = (int) TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS) + 1;
                user.setBonuses(user.getBonuses() - usedBonuses + (int)Math.ceil(mustPay * numberOfDays* bonusesPercent));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("amount", mustPay);

            userRepository.save(user);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplateBuilder.build().postForEntity(paymentsURL, requestEntity, String.class);

            entity = reservationRepository.save(reservation);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                userTransaction.rollback();
                user.setBonuses(oldUserBonuses);
                userRepository.save(user);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment wasn't successful");
            }

            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                userTransaction.rollback();
                user.setBonuses(oldUserBonuses);
                userRepository.save(user);
            } catch (SystemException e1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Reservation wasn't done", e1);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Reservation wasn't done", e);
        }

        return reservationMapper.entityToDto(entity);
    }

    private void validate(ReservationDTO reservationDTO) {
        if (reservationDTO.getArrivalDate().after(reservationDTO.getDepartureDate())) {
            throw new IllegalArgumentException("Arrival date can not be after departure date");
        }

        Matcher matcher = emailPattern.matcher(reservationDTO.getEmail());

        if (!matcher.find()) {
            throw new IllegalArgumentException("Email is invalid");
        }
    }

}
