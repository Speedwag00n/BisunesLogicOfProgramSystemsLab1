package ilia.nemankov.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotAHotelOwnerException extends RuntimeException {

    public NotAHotelOwnerException() {
        super();
    }

    public NotAHotelOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAHotelOwnerException(String message) {
        super(message);
    }

    public NotAHotelOwnerException(Throwable cause) {
        super(cause);
    }

}
