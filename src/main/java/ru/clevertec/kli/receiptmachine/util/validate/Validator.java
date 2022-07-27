package ru.clevertec.kli.receiptmachine.util.validate;

import ru.clevertec.kli.receiptmachine.exception.ValidationException;

public interface Validator<T> {

    void validate(T item) throws ValidationException;
}
