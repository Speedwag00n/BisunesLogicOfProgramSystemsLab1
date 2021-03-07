package ilia.nemankov.service;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.dto.RoomsDTO;
import ilia.nemankov.mapper.ConfigurationMapper;
import ilia.nemankov.mapper.OfferMapper;
import ilia.nemankov.mapper.RoomsMapper;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferMapper offerMapper;
    private final RoomsMapper roomsMapper;
    private final ConfigurationMapper configurationMapper;

    @Override
    public List<OfferDTO> createOffers(List<Configuration> entities) {
        List<OfferDTO> offers = new ArrayList<>();

        entities.sort(Comparator
                .<Configuration>comparingInt(element -> element.getRooms().getHotel().getId())
                .thenComparing(element -> element.getRooms().getId())
        );

        for (Configuration configuration : entities) {
            Rooms currentRooms = configuration.getRooms();
            Hotel currentHotel = currentRooms.getHotel();

            if (offers.size() == 0 || offers.get(offers.size() - 1).getHotelId().intValue() != currentHotel.getId().intValue()) {
                offers.add(offerMapper.entityToDto(currentHotel));
            }

            List<RoomsDTO> offerRooms = offers.get(offers.size() - 1).getRooms();

            if (offerRooms.size() == 0 || offerRooms.get(offerRooms.size() - 1).getRoomsId().intValue() != currentRooms.getId().intValue()) {
                offerRooms.add(roomsMapper.entityToDto(currentRooms));
            }

            List<ConfigurationDTO> roomsConfigurations = offerRooms.get(offerRooms.size() - 1).getConfigurations();

            roomsConfigurations.add(configurationMapper.entityToDto(configuration));
        }

        return offers;
    }

}
