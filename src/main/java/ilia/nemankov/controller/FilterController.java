package ilia.nemankov.controller;

import ilia.nemankov.dto.DirectionDTO;
import ilia.nemankov.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class FilterController {

    private final DirectionService directionService;

    @GetMapping("/api/directions")
    public List<DirectionDTO> getDirections() {
        return directionService.getDirections();
    }

    @GetMapping("/api/hotels")
    public void getSuitableOffers() {

    }

}
