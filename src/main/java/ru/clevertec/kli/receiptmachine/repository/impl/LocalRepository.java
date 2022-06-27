package ru.clevertec.kli.receiptmachine.repository.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;
import ru.clevertec.kli.receiptmachine.repository.Repository;

public class LocalRepository<E extends Entity<?>> implements Repository<E> {

    private final List<E> items;

    public LocalRepository(List<E> items) {
        this.items = items;
    }

    protected List<E> getItems() {
        return items;
    }

    @Override
    public void add(E item) {
        items.add(item);
    }

    @Override
    public E get(Object id) throws NoSuchElementException {
        return items.stream()
            .filter(item -> item.getId().equals(id))
            .findFirst()
            .orElseThrow();
    }

    @Override
    public List<E> getAll() {
        return items;
    }

    @Override
    public void remove(E item) {
        items.remove(item);
    }
}
