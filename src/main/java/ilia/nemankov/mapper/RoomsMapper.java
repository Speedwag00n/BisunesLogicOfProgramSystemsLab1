package ilia.nemankov.mapper;

import ilia.nemankov.dto.OfferDTO;
import ilia.nemankov.dto.RoomsDTO;
import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class RoomsMapper {

    public RoomsDTO entityToDto(Rooms entity) {
        RoomsDTO dto = new RoomsDTO();

        dto.setRoomsId(entity.getId());
        dto.setName(entity.getName());
        dto.setAvailableRooms(entity.getRoomsNumber());
        dto.setConfigurations(new ArrayList<>());

        return dto;
    }

}
