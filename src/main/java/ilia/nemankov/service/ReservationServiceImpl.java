package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationRequestDTO;
import ilia.nemankov.mapper.ReservationMapper;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    @Override
    public void makeReservation(ReservationRequestDTO reservationRequestDTO) {
        Reservation reservation = reservationMapper.dtoToEntity(reservationRequestDTO);

        reservationRepository.save(reservation);
    }

}
