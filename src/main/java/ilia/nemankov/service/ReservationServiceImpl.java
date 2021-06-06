package ilia.nemankov.service;

import ilia.nemankov.dto.RecommendationMessageDTO;
import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.mapper.ReservationMapper;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.model.User;
import ilia.nemankov.repository.ConfigurationRepository;
import ilia.nemankov.repository.ReservationRepository;
import ilia.nemankov.repository.UserRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.*;
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
    private final AmqpTemplate template;
    private final Queue queue;

    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper, ReservationRepository reservationRepository, ConfigurationService configurationService,
                                  UserRepository userRepository, ConfigurationRepository configurationRepository, RestTemplateBuilder restTemplateBuilder, UserTransaction userTransaction,
                                  AmqpTemplate template, Queue queue) {

        this.reservationMapper = reservationMapper;
        this.reservationRepository = reservationRepository;
        this.configurationService = configurationService;
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.restTemplateBuilder = restTemplateBuilder;
        this.userTransaction = userTransaction;
        this.template = template;
        this.queue = queue;

    }

    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern cardNumberPattern = Pattern.compile("^[0-9]{16}$", Pattern.CASE_INSENSITIVE);
    private final Pattern expiryDatePattern = Pattern.compile("^[0-9]{2}/[0-9]{2}$", Pattern.CASE_INSENSITIVE);
    private final Pattern cvcPattern = Pattern.compile("^[0-9]{3}$", Pattern.CASE_INSENSITIVE);

    @Value("${payments.url}")
    private String paymentsURL;

    @Value("${bonuses.percent}")
    private Double bonusesPercent;

    @Value("${bonuses.covering}")
    private Double bonusesCovering;

    @Override
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        Reservation entity;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).get();
        Integer oldUserBonuses = user.getBonuses();

        try {
            validate(reservationDTO);

            userTransaction.begin();

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
                int usedBonuses = (int)Math.ceil(mustPay * bonusesCovering) <= user.getBonuses() ? (int)Math.ceil(mustPay * bonusesCovering) : user.getBonuses();
                mustPay -= usedBonuses;
                long timeDifference = reservation.getDepartureDate().getTime() - reservation.getArrivalDate().getTime();
                int numberOfDays = (int) TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS) + 1;
                user.setBonuses(user.getBonuses() - usedBonuses + (int)Math.ceil(mustPay * numberOfDays* bonusesPercent));
            }

            entity = reservationRepository.save(reservation);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("cardNumber", reservationDTO.getCardNumber());
            body.put("expiryDate", reservationDTO.getExpiryDate());
            body.put("cvc", reservationDTO.getCvc());
            body.put("amount", mustPay);

            userRepository.save(user);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplateBuilder.build().postForEntity(paymentsURL, requestEntity, String.class);

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

        RecommendationMessageDTO message = new RecommendationMessageDTO();
        message.setUserId(user.getId());
        message.setConfigurationId(reservationDTO.getConfiguration());
        template.convertAndSend(queue.getName(), message);

        return reservationMapper.entityToDto(entity);
    }

    private void validate(ReservationDTO reservationDTO) {
        if (reservationDTO.getArrivalDate().after(reservationDTO.getDepartureDate())) {
            throw new IllegalArgumentException("Arrival date can not be after departure date");
        }

        Matcher emailMatcher = emailPattern.matcher(reservationDTO.getEmail());

        if (!emailMatcher.find()) {
            throw new IllegalArgumentException("Email is invalid");
        }

        Matcher cardNumberMatcher = cardNumberPattern.matcher(reservationDTO.getCardNumber());

        if (!cardNumberMatcher.find()) {
            throw new IllegalArgumentException("Card number is invalid");
        }

        Matcher expiryDateMatcher = expiryDatePattern.matcher(reservationDTO.getExpiryDate());

        if (!expiryDateMatcher.find()) {
            throw new IllegalArgumentException("Expiry date is invalid");
        }

        Matcher cvcMatcher = cvcPattern.matcher(reservationDTO.getCvc());

        if (!cvcMatcher.find()) {
            throw new IllegalArgumentException("CVC is invalid");
        }
    }

}
