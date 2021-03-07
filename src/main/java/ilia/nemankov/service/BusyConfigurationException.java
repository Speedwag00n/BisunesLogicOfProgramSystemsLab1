package ilia.nemankov.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusyConfigurationException extends RuntimeException {

    public BusyConfigurationException() {
        super();
    }

    public BusyConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusyConfigurationException(String message) {
        super(message);
    }

    public BusyConfigurationException(Throwable cause) {
        super(cause);
    }

}
