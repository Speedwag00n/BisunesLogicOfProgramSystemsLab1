package ilia.nemankov.service;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.model.Configuration;

import java.util.List;

public interface OfferService {

    List<OfferDTO> createOffers(List<Configuration> entities);

}
