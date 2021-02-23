package ilia.nemankov.dto;

import ilia.nemankov.model.HotelType;
import lombok.Data;

import java.util.List;

@Data
public class OfferDTO {

    private Integer hotelId;
    private String hotelName;
    private String address;
    private HotelType hotelType;
    private Integer stars;
    private String description;

    List<RoomsDTO> rooms;

}
