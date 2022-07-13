package ru.clevertec.kli.receiptmachine.exception;

import lombok.Getter;

@Getter
public class ValidationException extends Exception {

    private final String inputString;
    private final String validationPattern;

    public ValidationException(String inputString, String validationPattern) {
        this.inputString = inputString;
        this.validationPattern = validationPattern;
    }

    public ValidationException(String message, String inputString, String validationPattern) {
        super(message);
        this.inputString = inputString;
        this.validationPattern = validationPattern;
    }

    public ValidationException(String message, Throwable cause, String inputString,
        String validationPattern) {
        super(message, cause);
        this.inputString = inputString;
        this.validationPattern = validationPattern;
    }

    public ValidationException(Throwable cause, String inputString, String validationPattern) {
        super(cause);
        this.inputString = inputString;
        this.validationPattern = validationPattern;
    }
}
