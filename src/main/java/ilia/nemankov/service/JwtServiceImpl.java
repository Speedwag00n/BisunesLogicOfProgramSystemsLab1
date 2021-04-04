package ilia.nemankov.service;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.model.User;
import ilia.nemankov.repository.UserRepository;
import ilia.nemankov.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements UserDetailsService, JwtService {

    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public JwtUtils getJwtUtils() {
        return jwtUtils;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User with login " + login + " not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    @Override
    public String login(UserDTO user) {
        User entity = userRepository.findByLogin(user.getLogin());
        if (entity != null) {
            long id = entity.getId();
            return login(user, id);
        } else {
            throw new BadCredentialsException("Bad login");
        }
    }

    @Override
    public String login(UserDTO user, long id) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        UserDetails userDetails = loadUserByUsername(user.getLogin());
        return jwtUtils.generateToken(userDetails.getUsername(), id);
    }

    @Override
    public void logout(String login) {
        User entity = userRepository.findByLogin(login);

        Date lastLogout = new Date();
        entity.setLastLogout(lastLogout);

        userRepository.save(entity);
    }

    @Override
    public Date loadLastLogout(String login) {
        User entity = userRepository.findByLogin(login);
        return entity.getLastLogout();
    }

}
