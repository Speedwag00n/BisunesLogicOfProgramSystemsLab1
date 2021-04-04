package ilia.nemankov.controller;

import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.service.DirectionService;
import ilia.nemankov.service.FilterService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
public class FilterController {

    private final DirectionService directionService;
    private final FilterService filterService;

    @GetMapping("/api/directions")
    public ResponseEntity getDirections() {
        try {
            return ResponseEntity.ok(directionService.getDirections());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/hotels")
    public ResponseEntity getSuitableHotels(
            @RequestParam(value = "country", required = false) Integer country,
            @RequestParam(value = "city", required = false) Integer city,
            @RequestParam(value = "arrivalDate", required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date arrivalDate,
            @RequestParam(value = "departureDate", required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date departureDate,
            @RequestParam(value = "hotelTypes", required = false) List<String> hotelTypes,
            @RequestParam(value = "stars", required = false) List<Integer> stars,
            @RequestParam(value = "peopleNumber", required = true) Integer peopleNumber,
            @RequestParam(value = "food", required = false) List<String> food,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice
    ) {

        try {
            List<HotelDTO> results = filterService.getSuitableHotels(
                    country,
                    city,
                    arrivalDate,
                    departureDate,
                    hotelTypes,
                    stars,
                    peopleNumber,
                    food,
                    minPrice,
                    maxPrice
            );

            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
