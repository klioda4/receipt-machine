package ru.clevertec.kli.receiptmachine.repository;

import java.util.List;
import java.util.NoSuchElementException;

public interface Repository<T> {

    void add(T item);

    T get(int id) throws NoSuchElementException;

    List<T> getAll();

    void update(T item) throws NoSuchElementException;

    void remove(T item);
}
