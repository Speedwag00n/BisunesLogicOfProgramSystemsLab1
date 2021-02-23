package ilia.nemankov.service;

import ilia.nemankov.dto.ReservationRequestDTO;

public interface ReservationService {

    void makeReservation(ReservationRequestDTO reservationRequestDTO);

}
