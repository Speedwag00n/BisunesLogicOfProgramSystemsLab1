package ilia.nemankov.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @NotNull
    @Size(min=4, max=16)
    private String login;

    @NotNull
    @Size(min=6, max=16)
    private String password;

    @Positive
    private Integer bonuses;

}
