package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import java.util.NoSuchElementException;
import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;

public interface ProductService {

    List<ProductDto> getAll();

    ProductDto get(int id) throws NoSuchElementException;
}
