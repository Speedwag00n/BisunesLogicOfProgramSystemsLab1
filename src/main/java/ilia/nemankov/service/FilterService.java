package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;

import java.util.List;

public interface FilterService {

    List<OfferDTO> getSuitableHotels(
            String country,
            String city,
            List<String> hotel_type,
            List<Integer> stars,
            Integer peopleNumber,
            List<String> food,
            List<String> priceInterval
    );

}
