package ilia.nemankov.service;

import ilia.nemankov.dto.DirectionDTO;
import ilia.nemankov.model.Direction;
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
        List<Direction> directions =  directionRepository.findAll();
        List<DirectionDTO> results = new ArrayList<>();

        for (Direction direction : directions) {
            DirectionDTO dto = new DirectionDTO();
            dto.setDirection(direction.getName());
            results.add(dto);
        }

        return results;
    }

}
