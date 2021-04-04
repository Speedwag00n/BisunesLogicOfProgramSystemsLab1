package ilia.nemankov.service;

import ilia.nemankov.dto.HotelDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

public interface FilterService {

    List<HotelDTO> getSuitableHotels(
            Integer country,
            Integer city,
            Date arrivalDate,
            Date departureDate,
            List<String> hotelTypes,
            List<Integer> stars,
            @Positive Integer peopleNumber,
            List<String> food,
            @Min(0) Integer minPrice,
            @Min(0) Integer maxPrice
    );

}
