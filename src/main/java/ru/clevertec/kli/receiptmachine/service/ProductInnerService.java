package ru.clevertec.kli.receiptmachine.service;

import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;

/**
 * Provides methods for other services to work with products.
 */
public interface ProductInnerService {

    void writeOff(int id, int number) throws NotEnoughLeftoverException;
}
