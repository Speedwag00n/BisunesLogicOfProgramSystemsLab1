package ilia.nemankov.mapper;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class OfferMapper {

    public OfferDTO entityToDto(Hotel entity) {
        OfferDTO dto = new OfferDTO();

        dto.setHotelId(entity.getId());
        dto.setHotelName(entity.getName());
        dto.setAddress(entity.getAddress() + ", " + entity.getCity().getName() + ", " + entity.getCity().getCountry().getName());
        dto.setHotelType(entity.getHotelType());
        dto.setStars(entity.getStars());
        dto.setDescription(entity.getDescription());
        dto.setRooms(new ArrayList<>());

        return dto;
    }

}
