package ru.clevertec.kli.receiptmachine.util.io;

import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;

public interface ReceiptWriter {

    void write(ReceiptDto receipt);
}
