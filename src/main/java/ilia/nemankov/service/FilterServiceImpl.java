package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.mapper.OfferMapper;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.repository.HotelRepository;
import ilia.nemankov.repository.specification.FindHotelByStars;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FilterServiceImpl implements FilterService {

    private HotelRepository hotelRepository;
    private OfferMapper offerMapper;

    @Override
    public List<OfferDTO> getSuitableHotels(
            String country,
            String city,
            List<String> hotel_type,
            List<Integer> stars,
            Integer peopleNumber,
            List<String> food,
            List<String> priceInterval
    ) {
        Specification<Hotel> specification = new FindHotelByStars(stars);

        List<Hotel> hotels = hotelRepository.findAll(specification, PageRequest.of(0, 100)).getContent();

        List<OfferDTO> results = new ArrayList<>();
        for (Hotel hotel : hotels) {
            results.add(offerMapper.entityToDto(hotel));
        }

        return results;
    }

}
