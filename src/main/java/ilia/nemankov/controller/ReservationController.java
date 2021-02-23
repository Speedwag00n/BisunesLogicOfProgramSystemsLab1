package ilia.nemankov.controller;

import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservation")
    public ResponseEntity makeReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            return ResponseEntity.status(201).body(reservationService.makeReservation(reservationDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
