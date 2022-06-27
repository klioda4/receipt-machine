package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import java.util.NoSuchElementException;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;

public interface DiscountCardService {

    List<DiscountCardDto> getAll();

    DiscountCardDto get(int number) throws NoSuchElementException;
}
