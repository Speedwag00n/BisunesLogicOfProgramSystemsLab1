package ilia.nemankov.service;

import ilia.nemankov.dto.DirectionDTO;
import ilia.nemankov.model.City;
import ilia.nemankov.repository.DirectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DirectionServiceImpl implements DirectionService {

    private final DirectionRepository directionRepository;

    @Override
    public List<DirectionDTO> getDirections() {
        List<City> cities =  directionRepository.findAll();
        List<DirectionDTO> results = new ArrayList<>();

        for (City city : cities) {
            DirectionDTO dto = new DirectionDTO();
            dto.setDirection(city.getName() + ", " + city.getCountry().getName());
            dto.setCity(city.getId());
            dto.setCountry(city.getCountry().getId());
            results.add(dto);
        }

        return results;
    }

}
