package ilia.nemankov.service;

import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.model.Configuration;

import java.util.List;

public interface OfferService {

    List<HotelDTO> createOffers(List<Configuration> entities);

}
