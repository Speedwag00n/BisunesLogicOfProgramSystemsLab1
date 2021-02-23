package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.mapper.ReservationMapper;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.repository.ReservationRepository;
import ilia.nemankov.repository.specification.ReservationSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    @Override
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

        synchronized (this) {
            List<Reservation> existingReservations = reservationRepository.findAll(
                    new ReservationSpecification(
                            reservation.getConfiguration().getRooms().getId(),
                            reservation.getArrivalDate(),
                            reservation.getDepartureDate()
                    )
            );

            Map<Date, Integer> dates = new HashMap<>();
            int maximumNumberOfRooms = reservation.getConfiguration().getRooms().getRoomsNumber();

            for (Reservation existingReservation : existingReservations) {
                Date currentDate = existingReservation.getArrivalDate();
                while (!currentDate.after(existingReservation.getDepartureDate())) {
                    Integer currentValue = dates.get(currentDate);
                    currentValue = currentValue == null ? 1 : currentValue + 1;
                    dates.put(currentDate, currentValue);

                    if (currentValue == maximumNumberOfRooms) {
                        throw new IllegalArgumentException("There isn't any free room with selected configuration");
                    }

                    currentDate = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24));
                }
            }

            Reservation entity = reservationRepository.save(reservation);

            return reservationMapper.entityToDto(entity);
        }
    }

}
