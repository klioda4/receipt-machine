package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.ProductInnerService;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, ProductInnerService {

    private final Repository<Product> repository;
    private final ModelMapperExt mapper;

    @Override
    public List<ProductDto> getAll() {
        return mapper.mapList(repository.getAll(), ProductDto.class);
    }

    @Override
    public ProductDto get(int id) throws NoSuchElementException {
        return mapper.map(repository.get(id), ProductDto.class);
    }

    @Override
    public void writeOff(int id, int number) throws NotEnoughLeftoverException {
        if (number < 0) {
            throw new IllegalArgumentException("number can't be less than 0");
        }
        Product product = repository.get(id);
        checkLeftover(product, number);
        product.setCount(
            product.getCount() - number);
    }

    private void checkLeftover(Product product, int neededCount) throws NotEnoughLeftoverException {
        if (product.getCount() < neededCount) {
            throw new NotEnoughLeftoverException(neededCount, product.getCount());
        }
    }
}
