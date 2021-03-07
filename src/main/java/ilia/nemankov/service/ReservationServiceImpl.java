package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.mapper.ReservationMapper;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final ConfigurationService configurationService;

    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        validate(reservationDTO);

        Reservation reservation = reservationMapper.dtoToEntity(reservationDTO);

        if (!configurationService.isConfigurationAvailable(
                reservation.getConfiguration(),
                reservation.getArrivalDate(),
                reservation.getDepartureDate())
        ) {
            throw new BusyConfigurationException("There isn't any free room with selected configuration");
        }

        Reservation entity = reservationRepository.save(reservation);

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
