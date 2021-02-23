package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.mapper.OfferMapper;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.repository.ConfigurationRepository;
import ilia.nemankov.repository.specification.ConfigurationSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Validated
public class FilterServiceImpl implements FilterService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationService configurationService;
    private final OfferMapper offerMapper;

    @Override
    public List<OfferDTO> getSuitableHotels(
            Integer country,
            Integer city,
            Date arrivalDate,
            Date departureDate,
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

        Specification<Configuration> specification = new ConfigurationSpecification(city, country, hotelTypes, stars, food, peopleNumber, minPrice, maxPrice);

        List<Configuration> configurations = configurationRepository.findAll(specification);

        configurations = configurations.stream().filter(element -> configurationService.isConfigurationAvailable(element, arrivalDate, departureDate)).collect(Collectors.toList());

        return offerMapper.entitiesToDtos(configurations);
    }

}
