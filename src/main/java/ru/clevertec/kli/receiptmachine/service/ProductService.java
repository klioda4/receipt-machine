package ru.clevertec.kli.receiptmachine.service;

import java.util.List;
import java.util.NoSuchElementException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.update.ProductUpdateDto;
import ru.clevertec.kli.receiptmachine.util.enums.PutResult;

public interface ProductService {

    List<ProductDto> getAll();

    ProductDto get(int id) throws NoSuchElementException;

    ProductDto add(ProductDto productDto);

    void delete(int id);

    ProductDto update(int id, ProductUpdateDto productUpdateDto) throws NoSuchElementException;

    PutResult put(ProductDto newItem);
}
