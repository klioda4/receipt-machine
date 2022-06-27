package ru.clevertec.kli.receiptmachine.util.parse;

import java.util.Optional;

public interface Parsable<T> {

    Optional<T> tryParse(String item);
}
