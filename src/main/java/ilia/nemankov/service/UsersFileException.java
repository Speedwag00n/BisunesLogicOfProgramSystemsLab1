package ilia.nemankov.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UsersFileException extends RuntimeException {

    public UsersFileException() {
        super();
    }

    public UsersFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsersFileException(String message) {
        super(message);
    }

    public UsersFileException(Throwable cause) {
        super(cause);
    }

}
