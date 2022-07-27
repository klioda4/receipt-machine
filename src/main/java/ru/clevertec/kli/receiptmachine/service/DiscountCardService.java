package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import java.util.NoSuchElementException;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.update.DiscountCardUpdateDto;
import ru.clevertec.kli.receiptmachine.util.enums.PutResult;

public interface DiscountCardService {

    List<DiscountCardDto> getAll();

    DiscountCardDto get(int number) throws NoSuchElementException;

    DiscountCardDto add(DiscountCardDto discountCardDto);

    DiscountCardDto update(int number, DiscountCardUpdateDto updateDto) throws NoSuchElementException;

    PutResult put(DiscountCardDto newItem);

    void delete(int number);
}
