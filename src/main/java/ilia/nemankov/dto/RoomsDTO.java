package ilia.nemankov.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomsDTO {

    private String name;
    private Integer availableRooms;

    private List<ConfigurationDTO> configurations;

}
