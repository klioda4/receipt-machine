package ru.clevertec.kli.receiptmachine.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;
import ru.clevertec.kli.receiptmachine.repository.Repository;

public class LocalRepository<E extends Entity> implements Repository<E> {

    private final List<E> items;

    public LocalRepository(List<E> items) {
        this.items = new ArrayList<>(items);
    }

    protected List<E> getItems() {
        return items;
    }

    @Override
    public void add(E item) {
        item.setId(new Random().nextInt(10000));
        items.add(item);
    }

    @Override
    public E get(int id) throws NoSuchElementException {
        return items.stream()
            .filter(item -> id == item.getId())
            .findFirst()
            .orElseThrow();
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(items);
    }

    @Override
    public void update(E item) throws NoSuchElementException {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == item.getId()) {
                items.set(i, item);
                return;
            }
        }
    }

    @Override
    public void remove(E item) {
        Iterator<E> iterator = items.iterator();
        while (iterator.hasNext()) {
            E nextItem = iterator.next();
            if (nextItem.getId() == item.getId()) {
                iterator.remove();
                break;
            }
        }
    }
}
