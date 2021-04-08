package ilia.nemankov.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginInUseException extends RuntimeException {

    public LoginInUseException() {
        super();
    }

    public LoginInUseException(String message) {
        super(message);
    }

    public LoginInUseException(Throwable cause) {
        super(cause);
    }

    public LoginInUseException(String message, Throwable cause) {
        super(message, cause);
    }

}
