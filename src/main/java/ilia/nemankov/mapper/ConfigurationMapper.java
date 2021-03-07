package ilia.nemankov.mapper;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.model.Configuration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

}
