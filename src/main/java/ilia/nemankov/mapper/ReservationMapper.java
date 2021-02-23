package ilia.nemankov.mapper;

import ilia.nemankov.dto.ReservationDTO;
import ilia.nemankov.model.Reservation;
import ilia.nemankov.repository.ConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationMapper {

    private final ConfigurationRepository configurationRepository;

    public Reservation dtoToEntity(ReservationDTO dto) {
        Reservation entity = new Reservation();

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setArrivalDate(dto.getArrivalDate());
        entity.setDepartureDate(dto.getDepartureDate());
        entity.setConfiguration(configurationRepository.findById(dto.getConfiguration()).orElseThrow(IllegalArgumentException::new));

        return entity;
    }

    public ReservationDTO entityToDto(Reservation entity) {
        ReservationDTO dto = new ReservationDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setArrivalDate(entity.getArrivalDate());
        dto.setDepartureDate(entity.getDepartureDate());
        dto.setConfiguration(entity.getConfiguration().getId());

        return dto;
    }

}
