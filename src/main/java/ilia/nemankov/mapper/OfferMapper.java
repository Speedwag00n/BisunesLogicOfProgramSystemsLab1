package ilia.nemankov.mapper;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.dto.RoomsDTO;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.Rooms;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class OfferMapper {

    public List<OfferDTO> entitiesToDtos(List<Configuration> entities) {
        List<OfferDTO> dtos = new ArrayList<>();

        entities.sort(Comparator
                .<Configuration>comparingInt(element -> element.getRooms().getHotel().getId())
                .thenComparing(element -> element.getRooms().getId())
        );

        for (Configuration configuration : entities) {
            Rooms currentRooms = configuration.getRooms();
            Hotel currentHotel = currentRooms.getHotel();

            if (dtos.size() == 0 || dtos.get(dtos.size() - 1).getHotelId().intValue() != currentHotel.getId().intValue()) {
                OfferDTO offerDTO = new OfferDTO();

                offerDTO.setHotelId(currentHotel.getId());
                offerDTO.setHotelName(currentHotel.getName());
                offerDTO.setAddress(currentHotel.getAddress() + ", " + currentHotel.getCity().getName() + ", " + currentHotel.getCity().getCountry().getName());
                offerDTO.setHotelType(currentHotel.getHotelType());
                offerDTO.setStars(currentHotel.getStars());
                offerDTO.setDescription(currentHotel.getDescription());
                offerDTO.setRooms(new ArrayList<>());

                dtos.add(offerDTO);
            }

            List<RoomsDTO> offerRooms = dtos.get(dtos.size() - 1).getRooms();

            if (offerRooms.size() == 0 || offerRooms.get(offerRooms.size() - 1).getRoomsId().intValue() != currentRooms.getId().intValue()) {
                RoomsDTO roomsDTO = new RoomsDTO();

                roomsDTO.setRoomsId(currentRooms.getId());
                roomsDTO.setName(currentRooms.getName());
                roomsDTO.setAvailableRooms(currentRooms.getRoomsNumber());
                roomsDTO.setConfigurations(new ArrayList<>());

                offerRooms.add(roomsDTO);
            }

            List<ConfigurationDTO> roomsConfigurations = offerRooms.get(offerRooms.size() - 1).getConfigurations();

            ConfigurationDTO configurationDTO = new ConfigurationDTO();

            configurationDTO.setConfigurationId(configuration.getId());
            configurationDTO.setCapacity(configuration.getCapacity());
            configurationDTO.setFoodType(configuration.getFoodType());
            configurationDTO.setPrice(configuration.getPrice());

            roomsConfigurations.add(configurationDTO);
        }

        return dtos;
    }

}
