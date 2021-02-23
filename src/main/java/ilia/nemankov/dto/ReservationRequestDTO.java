package ilia.nemankov.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationRequestDTO {

    private String name;
    private String surname;
    private String email;
    private Integer configuration;
    private Date arrivalDate;
    private Date departureDate;

}
