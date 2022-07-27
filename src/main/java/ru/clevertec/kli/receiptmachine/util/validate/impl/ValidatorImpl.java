package ru.clevertec.kli.receiptmachine.util.validate.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;
import ru.clevertec.kli.receiptmachine.util.serialize.strings.StringConverter;
import ru.clevertec.kli.receiptmachine.util.validate.Validator;

@RequiredArgsConstructor
public class ValidatorImpl<T> implements Validator<T> {

    private final StringConverter<T> converter;
    private final Parser<T> parser;

    @Override
    public void validate(T item) throws ValidationException {
        String string = converter.convertToString(item);
        parser.parse(string);
    }
}
