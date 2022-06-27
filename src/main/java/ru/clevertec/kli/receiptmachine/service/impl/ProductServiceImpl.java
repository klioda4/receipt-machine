package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Repository<Product> repository;
    private final ModelMapperExt mapper;

    @Override public List<ProductDto> getAll() {
        return mapper.mapList(repository.getAll(), ProductDto.class);
    }

    @Override public ProductDto get(int id) throws NoSuchElementException {
        return mapper.map(repository.get(id), ProductDto.class);
    }
}
