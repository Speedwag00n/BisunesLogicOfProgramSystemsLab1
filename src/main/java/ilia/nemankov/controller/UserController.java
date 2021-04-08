package ilia.nemankov.controller;

import ilia.nemankov.dto.UserDTO;
import ilia.nemankov.service.JwtService;
import ilia.nemankov.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<String> register(@Validated @RequestBody UserDTO dto) {
        long id = userService.addUser(dto);
        String token = jwtService.login(dto, id);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
