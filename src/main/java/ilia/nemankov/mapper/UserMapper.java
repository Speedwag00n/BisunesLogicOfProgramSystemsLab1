package ilia.nemankov.mapper;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private PasswordEncoder passwordEncoder;

    public User dtoToEntity(UserDTO dto) {
        User entity = new User();

        entity.setLogin(dto.getLogin());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        return entity;
    }

    public UserDTO entityToDto(User entity) {
        UserDTO dto = new UserDTO();

        dto.setLogin(entity.getLogin());
        dto.setBonuses(entity.getBonuses());

        return dto;
    }

}
