package ru.clevertec.kli.receiptmachine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputStringException extends Exception{

    private final String inputString;

    public InvalidInputStringException(String inputString) {
        this.inputString = inputString;
    }

    public InvalidInputStringException(String message, String inputString) {
        super(message);
        this.inputString = inputString;
    }

    public InvalidInputStringException(String message, Throwable cause, String inputString) {
        super(message, cause);
        this.inputString = inputString;
    }

    public InvalidInputStringException(Throwable cause, String inputString) {
        super(cause);
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }
}
