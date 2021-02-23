package ilia.nemankov.controller;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.service.DirectionService;
import ilia.nemankov.service.FilterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "hotel_type", required = false) List<String> hotelTypes,
            @RequestParam(value = "stars", required = false) List<Integer> stars,
            @RequestParam(value = "people_number", required = true) Integer peopleNumber,
            @RequestParam(value = "food", required = false) List<String> food,
            @RequestParam(value = "min_price", required = false) Integer minPrice,
            @RequestParam(value = "max_price", required = false) Integer maxPrice
    ) {

        try {
            List<OfferDTO> results = filterService.getSuitableHotels(
                    country,
                    city,
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
