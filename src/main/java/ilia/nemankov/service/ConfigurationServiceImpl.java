package ilia.nemankov.service;

import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.repository.ReservationRepository;
import ilia.nemankov.repository.specification.ReservationSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ReservationRepository reservationRepository;

    @Override
    public boolean isConfigurationAvailable(Configuration configuration, Date arrivalDate, Date departureDate) {
        List<Reservation> existingReservations = reservationRepository.findAll(new ReservationSpecification(configuration.getRooms().getId(), arrivalDate, departureDate));

        Map<Date, Integer> dates = new HashMap<>();

        for (Reservation existingReservation : existingReservations) {
            Date currentDate = existingReservation.getArrivalDate();
            while (!currentDate.after(existingReservation.getDepartureDate())) {
                Integer currentValue = dates.get(currentDate);
                currentValue = currentValue == null ? 1 : currentValue + 1;
                dates.put(currentDate, currentValue);

                if (currentValue == configuration.getRooms().getRoomsNumber().intValue()) {
                    return false;
                }

                currentDate = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24));
            }
        }

        return true;
    }

}
