package ilia.nemankov.controller;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.service.JwtService;
import ilia.nemankov.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class AuthController {

    private final JwtService jwtService;
    private final JwtUtils jwtUtils;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO dto) {
        String token = jwtService.login(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization") String tokenHeader) {
        String token = jwtUtils.getTokenFromHeader(tokenHeader);
        String login = jwtUtils.getUsernameFromToken(token);

        jwtService.logout(login);

        return new ResponseEntity(HttpStatus.OK);
    }

}
