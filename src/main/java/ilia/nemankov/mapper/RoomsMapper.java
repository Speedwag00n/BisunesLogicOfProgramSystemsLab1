package ilia.nemankov.mapper;

import ilia.nemankov.dto.RoomsDTO;
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

    public Rooms dtoToEntity(RoomsDTO dto) {
        Rooms entity = new Rooms();

        entity.setId(dto.getRoomsId());
        entity.setName(dto.getName());
        entity.setRoomsNumber(dto.getAvailableRooms());

        return entity;
    }

}
