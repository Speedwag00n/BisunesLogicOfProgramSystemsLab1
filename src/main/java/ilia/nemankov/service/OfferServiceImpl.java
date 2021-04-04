package ilia.nemankov.service;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.dto.ConvenienceDTO;
import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.dto.RoomsDTO;
import ilia.nemankov.mapper.ConfigurationMapper;
import ilia.nemankov.mapper.ConvenienceMapper;
import ilia.nemankov.mapper.HotelMapper;
import ilia.nemankov.mapper.RoomsMapper;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Convenience;
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

    private final HotelMapper offerMapper;
    private final RoomsMapper roomsMapper;
    private final ConfigurationMapper configurationMapper;
    private final ConvenienceMapper convenienceMapper;

    @Override
    public List<HotelDTO> createOffers(List<Configuration> entities) {
        List<HotelDTO> offers = new ArrayList<>();

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

            ConfigurationDTO configurationDTO = configurationMapper.entityToDto(configuration);

            List<ConvenienceDTO> conveniences = new ArrayList<>();
            for (Convenience convenience : configuration.getConveniences()) {
                conveniences.add(convenienceMapper.entityToDto(convenience));
            }
            configurationDTO.setConveniences(conveniences);

            roomsConfigurations.add(configurationDTO);
        }

        return offers;
    }

}
