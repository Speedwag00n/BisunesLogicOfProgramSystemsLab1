package ilia.nemankov.service;

import ilia.nemankov.dto.HotelDTO;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Hotel;

import javax.transaction.SystemException;
import java.util.List;

public interface HotelService {

    void editHotel(HotelDTO hotelDTO);

    HotelDTO getHotel(Integer id);

}
