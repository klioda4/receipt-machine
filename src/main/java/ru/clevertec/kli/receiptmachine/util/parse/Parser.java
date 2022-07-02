package ru.clevertec.kli.receiptmachine.util.parse;

import ru.clevertec.kli.receiptmachine.exception.ValidationException;

public interface Parser<T> {

    T parse(String item) throws ValidationException;
}
