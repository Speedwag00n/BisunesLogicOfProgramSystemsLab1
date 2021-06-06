package ilia.nemankov.controller;

import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
public class HotelController {

    private final HotelService hotelService;

    @PutMapping("/api/hotel/update")
    public ResponseEntity updateHotel(@RequestBody HotelDTO hotelDTO) {
        try {
            hotelService.editHotel(hotelDTO);
            return ResponseEntity.status(200).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
