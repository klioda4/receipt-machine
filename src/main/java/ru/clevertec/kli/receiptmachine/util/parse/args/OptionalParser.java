package ru.clevertec.kli.receiptmachine.util.parse.args;

import java.util.Optional;

public interface OptionalParser<T> {

    Optional<T> tryParse(String item);
}
