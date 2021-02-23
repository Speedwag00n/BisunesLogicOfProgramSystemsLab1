package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;

import javax.validation.constraints.Min;
import java.util.List;

public interface FilterService {

    List<OfferDTO> getSuitableHotels(
            Integer country,
            Integer city,
            List<String> hotelTypes,
            List<Integer> stars,
            Integer peopleNumber,
            List<String> food,
            @Min(0) Integer minPrice,
            @Min(0) Integer maxPrice
    );

}
