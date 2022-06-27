package ru.clevertec.kli.receiptmachine.repository;

import java.util.List;
import java.util.NoSuchElementException;

public interface Repository<T> {

    void add(T item);

    T get(Object id) throws NoSuchElementException;

    List<T> getAll();

    void remove(T item);
}
