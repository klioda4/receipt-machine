package ru.clevertec.kli.receiptmachine.exception;

import lombok.Getter;

@Getter
public class NotEnoughLeftoverException extends Exception{

    private final int requestedQuantity;
    private final int availableQuantity;

    public NotEnoughLeftoverException(int requestedQuantity, int availableQuantity) {
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public NotEnoughLeftoverException(String message, int requestedQuantity, int availableQuantity) {
        super(message);
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public NotEnoughLeftoverException(String message, Throwable cause, int requestedQuantity,
        int availableQuantity) {
        super(message, cause);
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public NotEnoughLeftoverException(Throwable cause, int requestedQuantity, int availableQuantity) {
        super(cause);
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
}
