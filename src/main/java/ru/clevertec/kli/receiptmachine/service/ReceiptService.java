package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import java.util.NoSuchElementException;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;

public interface ReceiptService {

    ReceiptDto get(int id) throws NoSuchElementException;

    List<ReceiptDto> getAll();

    ReceiptDto add(List<PurchaseDto> purchaseDtos, CardDto card)
        throws NoSuchElementException;

    ReceiptDto add(String[] receiptArgs) throws InvalidInputStringException;

    void delete(int id);
}
