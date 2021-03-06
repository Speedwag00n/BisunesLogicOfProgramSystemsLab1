package ilia.nemankov.dto;

import ilia.nemankov.model.FoodType;
import lombok.Data;

import java.util.List;

@Data
public class ConfigurationDTO {

    private Integer configurationId;
    private Integer capacity;
    private FoodType foodType;
    private Integer price;
    private List<ConvenienceDTO> conveniences;

}
