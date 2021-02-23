package ilia.nemankov.controller;

import ilia.nemankov.dto.ReservationRequestDTO;
import ilia.nemankov.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservation")
    public void makeReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        reservationService.makeReservation(reservationRequestDTO);
    }

}
