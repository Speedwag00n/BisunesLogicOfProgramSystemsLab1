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
import ilia.nemankov.repository.CityRepository;
import ilia.nemankov.repository.ConfigurationRepository;
import ilia.nemankov.repository.HotelRepository;
import ilia.nemankov.repository.RoomsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelMapper hotelMapper;
    private final RoomsMapper roomsMapper;
    private final ConfigurationMapper configurationMapper;
    private final ConvenienceMapper convenienceMapper;

    private final HotelRepository hotelRepository;
    private final RoomsRepository roomsRepository;
    private final ConfigurationRepository configurationRepository;
    private final CityRepository cityRepository;

    @Override
    public void editHotel(HotelDTO hotelDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Hotel currentHotel = hotelRepository.findById(hotelDTO.getHotelId()).get();
        Hotel hotel = hotelMapper.dtoToEntity(hotelDTO);

        if (authentication == null || (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))
                && !currentHotel.getOwner().equals(authentication.getName()))) {
            throw new NotAHotelOwnerException("Current user isn't an admin or an owner of the current hotel");
        }

        hotel.setCity(cityRepository.findById(hotelDTO.getCityId()).get());
        hotel.setOwner(currentHotel.getOwner());

        List<Rooms> roomsList = new ArrayList<>();
        for (RoomsDTO roomsDTO : hotelDTO.getRooms()) {
            Rooms rooms = roomsMapper.dtoToEntity(roomsDTO);
            rooms.setHotel(hotel);
            roomsList.add(rooms);

            List<Configuration> configurationList = new ArrayList<>();
            for (ConfigurationDTO configurationDTO : roomsDTO.getConfigurations()) {
                Configuration configuration = configurationMapper.dtoToEntity(configurationDTO);
                configuration.setRooms(rooms);
                List<Convenience> conveniences = new ArrayList<>();
                configuration.setConveniences(conveniences);

                for (ConvenienceDTO convenienceDTO : configurationDTO.getConveniences()) {
                    Convenience convenience = convenienceMapper.dtoToEntity(convenienceDTO);
                    conveniences.add(convenience);
                }
                configurationList.add(configuration);
            }
            rooms.setConfigurations(configurationList);
        }
        hotel.setRooms(roomsList);

        hotelRepository.save(hotel);
    }

    public HotelDTO getHotel(Integer id) {
        Hotel hotel = hotelRepository.findById(id).get();

        HotelDTO hotelDTO = hotelMapper.entityToDto(hotel);
        List<RoomsDTO> roomsDTOs = new ArrayList<>();
        hotelDTO.setRooms(roomsDTOs);

        for (Rooms rooms : hotel.getRooms()) {
            RoomsDTO roomsDTO = roomsMapper.entityToDto(rooms);
            roomsDTOs.add(roomsDTO);
            List<ConfigurationDTO> configurationDTOs = new ArrayList<>();
            roomsDTO.setConfigurations(configurationDTOs);

            for (Configuration configuration : rooms.getConfigurations()) {
                ConfigurationDTO configurationDTO = configurationMapper.entityToDto(configuration);
                configurationDTOs.add(configurationDTO);
                List<ConvenienceDTO> convenienceDTOs = new ArrayList<>();
                configurationDTO.setConveniences(convenienceDTOs);

                for (Convenience convenience : configuration.getConveniences()) {
                    ConvenienceDTO convenienceDTO = convenienceMapper.entityToDto(convenience);
                    convenienceDTOs.add(convenienceDTO);
                }
            }
        }

        return hotelDTO;
    }

}
