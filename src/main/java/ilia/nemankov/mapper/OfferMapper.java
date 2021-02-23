package ilia.nemankov.mapper;

import ilia.nemankov.dto.ConfigurationDTO;
import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.dto.RoomsDTO;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.Rooms;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OfferMapper {

    public OfferDTO entityToDto(Hotel hotel) {
        OfferDTO offerDTO = new OfferDTO();

        offerDTO.setHotelName(hotel.getName());
        offerDTO.setAddress(hotel.getAddress() + ", " + hotel.getCity().getName() + ", " + hotel.getCity().getCountry().getName());
        offerDTO.setHotelType(hotel.getHotelType());
        offerDTO.setStars(hotel.getStars());
        offerDTO.setDescription(hotel.getDescription());
        offerDTO.setRooms(new ArrayList<>());

        for (Rooms rooms : hotel.getRooms()) {
            RoomsDTO roomsDTO = new RoomsDTO();

            roomsDTO.setName(rooms.getName());
            roomsDTO.setAvailableRooms(rooms.getRoomsNumber());
            roomsDTO.setConfigurations(new ArrayList<>());

            offerDTO.getRooms().add(roomsDTO);

            for (Configuration configuration : rooms.getConfigurations()) {
                ConfigurationDTO configurationDTO = new ConfigurationDTO();

                configurationDTO.setCapacity(configuration.getCapacity());
                configurationDTO.setFoodType(configuration.getFoodType());
                configurationDTO.setPrice(configuration.getPrice());

                roomsDTO.getConfigurations().add(configurationDTO);
            }
        }

        return offerDTO;
    }

}
