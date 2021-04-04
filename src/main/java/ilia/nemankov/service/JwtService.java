package ilia.nemankov.service;

import ilia.nemankov.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

public interface JwtService {

    UserDetails loadUserByUsername(String login) throws UsernameNotFoundException;

    String login(UserDTO user);

    String login(UserDTO user, long id);

    void logout(String login);

    Date loadLastLogout(String login);

}
