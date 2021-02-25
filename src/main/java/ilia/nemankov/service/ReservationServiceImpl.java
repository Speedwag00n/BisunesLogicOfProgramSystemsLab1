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

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getArrivalDate().after(reservationDTO.getDepartureDate())) {
            throw new IllegalArgumentException("Arrival date can not be after departure date");
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(reservationDTO.getEmail());

        if (!matcher.find()) {
            System.out.println(reservationDTO.getEmail());
            throw new IllegalArgumentException("Email is invalid");
        }

        Reservation reservation = reservationMapper.dtoToEntity(reservationDTO);

        if (!configurationService.isConfigurationAvailable(
                reservation.getConfiguration(),
                reservation.getArrivalDate(),
                reservation.getDepartureDate())
        ) {
            throw new IllegalArgumentException("There isn't any free room with selected configuration");
        }

        Reservation entity = reservationRepository.save(reservation);

        return reservationMapper.entityToDto(entity);
    }

}
