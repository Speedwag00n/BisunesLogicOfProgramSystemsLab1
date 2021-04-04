package ilia.nemankov.mapper;

import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class HotelMapper {

    public HotelDTO entityToDto(Hotel entity) {
        HotelDTO dto = new HotelDTO();

        dto.setHotelId(entity.getId());
        dto.setHotelName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setCityId(entity.getCity().getId());
        dto.setCityName(entity.getCity().getName());
        dto.setCountryName(entity.getCity().getCountry().getName());
        dto.setHotelType(entity.getHotelType());
        dto.setStars(entity.getStars());
        dto.setDescription(entity.getDescription());
        dto.setRooms(new ArrayList<>());

        return dto;
    }

    public Hotel dtoToEntity(HotelDTO dto) {
        Hotel entity = new Hotel();

        entity.setId(dto.getHotelId());
        entity.setName(dto.getHotelName());
        entity.setAddress(dto.getAddress());
        entity.setHotelType(dto.getHotelType());
        entity.setStars(dto.getStars());
        entity.setDescription(dto.getDescription());
        entity.setRooms(new ArrayList<>());

        return entity;
    }

}
