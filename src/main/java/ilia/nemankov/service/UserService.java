package ilia.nemankov.service;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.service.LoginInUseException;

import java.util.List;

public interface UserService {

    long addUser(UserDTO user) throws LoginInUseException;

    List<UserDTO> getAllUsers();

    void updateUserBonuses(UserDTO userDTO);

}
