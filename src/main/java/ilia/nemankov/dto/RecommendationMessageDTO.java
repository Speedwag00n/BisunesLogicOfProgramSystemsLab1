package ilia.nemankov.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecommendationMessageDTO implements Serializable {

    private long userId;
    private int configurationId;

}
