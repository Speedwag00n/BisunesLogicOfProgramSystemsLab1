package ilia.nemankov.service;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.mapper.UserMapper;
import ilia.nemankov.model.User;
import ilia.nemankov.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public long addUser(UserDTO user) throws LoginInUseException {
        User entity = userMapper.dtoToEntity(user);
        Date registrationTime = new Date();
        entity.setLastLogout(registrationTime);
        Set<String> roles = new HashSet<>();
        entity.setRoles(roles);
        try {
            return userRepository.save(entity).getId();
        } catch (Throwable e) {
            throw e;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<UserDTO> result = new ArrayList<>();
        for (User user : users) {
            result.add(userMapper.entityToDto(user));
        }

        return result;
    }

    @Override
    public void updateUserBonuses(UserDTO userDTO) {
        User user = userRepository.findByLogin(userDTO.getLogin());

        user.setBonuses(userDTO.getBonuses());

        userRepository.save(user);
    }

}
