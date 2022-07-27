package ru.clevertec.kli.receiptmachine.util.serialize.strings;

public interface StringConverter<T> {

    String convertToString(T item);
}
