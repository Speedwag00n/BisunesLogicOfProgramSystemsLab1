package ilia.nemankov.mapper;

import ilia.nemankov.dto.ConvenienceDTO;
import ilia.nemankov.model.Convenience;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConvenienceMapper {

    public ConvenienceDTO entityToDto(Convenience entity) {
        ConvenienceDTO dto = new ConvenienceDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public Convenience dtoToEntity(ConvenienceDTO dto) {
        Convenience entity = new Convenience();

        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }

}
