package ilia.nemankov.mapper;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Convenience;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ConfigurationMapper {

    public ConfigurationDTO entityToDto(Configuration entity) {
        ConfigurationDTO dto = new ConfigurationDTO();

        dto.setConfigurationId(entity.getId());
        dto.setCapacity(entity.getCapacity());
        dto.setFoodType(entity.getFoodType());
        dto.setPrice(entity.getPrice());

        return dto;
    }

    public Configuration dtoToEntity(ConfigurationDTO dto) {
        Configuration entity = new Configuration();

        entity.setId(dto.getConfigurationId());
        entity.setCapacity(dto.getCapacity());
        entity.setFoodType(dto.getFoodType());
        entity.setPrice(dto.getPrice());

        return entity;
    }

}
