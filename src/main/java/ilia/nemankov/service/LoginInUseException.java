package ilia.nemankov.service;

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
