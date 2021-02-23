package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.mapper.OfferMapper;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.repository.HotelRepository;
import ilia.nemankov.repository.specification.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class FilterServiceImpl implements FilterService {

    private HotelRepository hotelRepository;
    private OfferMapper offerMapper;

    @Override
    public List<OfferDTO> getSuitableHotels(
            Integer country,
            Integer city,
            List<String> hotelTypes,
            List<Integer> stars,
            Integer peopleNumber,
            List<String> food,
            @Min(0) Integer minPrice,
            @Min(0) Integer maxPrice
    ) {
        if (city == null && country == null) {
            throw new IllegalArgumentException("city or country must be specified");
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice can not be greater than maxPrice");
        }

        Specification<Hotel> specification = new HotelSpecification(city, country, hotelTypes, stars, food, peopleNumber, minPrice, maxPrice);

        List<Hotel> hotels = hotelRepository.findAll(specification, PageRequest.of(0, 100)).getContent();

        List<OfferDTO> results = new ArrayList<>();
        for (Hotel hotel : hotels) {
            results.add(offerMapper.entityToDto(hotel));
        }

        return results;
    }

}
