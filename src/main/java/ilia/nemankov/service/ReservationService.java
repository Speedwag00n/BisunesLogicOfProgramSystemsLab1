package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationDTO;

public interface ReservationService {

    ReservationDTO makeReservation(ReservationDTO reservationRequestDTO);

}
