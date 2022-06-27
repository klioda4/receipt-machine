package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;

public interface ReceiptPositionService {

    void add(ReceiptPosition position);

    void addAll(List<ReceiptPosition> positions);

    List<ReceiptPosition> getByReceiptId(int receiptId);

    List<ReceiptPositionDto> getDtosByReceiptId(int receiptId);
}
