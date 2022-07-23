package ru.clevertec.kli.receiptmachine.exception;

import lombok.Getter;

@Getter
public class ValidationException extends Exception {

    private final String inputString;

    public ValidationException(String message, String inputString) {
        super(message);
        this.inputString = inputString;
    }

    public ValidationException(String message, Throwable cause, String inputString) {
        super(message, cause);
        this.inputString = inputString;
    }

    public ValidationException(Throwable cause, String inputString) {
        super(cause);
        this.inputString = inputString;
    }
}
