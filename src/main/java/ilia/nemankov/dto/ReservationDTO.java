package ilia.nemankov.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReservationDTO {

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String email;
    @NotNull
    private Integer configuration;
    @NotNull
    private Date arrivalDate;
    @NotNull
    private Date departureDate;

}
