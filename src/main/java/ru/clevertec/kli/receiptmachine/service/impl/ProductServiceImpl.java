package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.update.ProductUpdateDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.ProductInnerService;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.Transactional;
import ru.clevertec.kli.receiptmachine.util.enums.PutResult;
import ru.clevertec.kli.receiptmachine.util.validate.Validator;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, ProductInnerService {

    private final Repository<Product> repository;
    private final ModelMapperExt mapper;
    private final Validator<Product> validator;

    @Override
    public List<ProductDto> getAll() {
        return mapper.mapList(repository.getAll(), ProductDto.class);
    }

    @Override
    public ProductDto get(int id) throws NoSuchElementException {
        return mapper.map(repository.get(id), ProductDto.class);
    }

    @Override
    @CallsLog
    public ProductDto add(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        validator.validate(product);
        repository.add(product);
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public void delete(int id) {
        Product deleteProduct = Product.builder()
            .id(id)
            .build();
        repository.remove(deleteProduct);
    }

    @Override
    @Transactional
    public ProductDto update(int id, ProductUpdateDto updateDto) throws NoSuchElementException {
        Product product = repository.get(id);
        updateFields(product, updateDto);
        validator.validate(product);
        repository.update(product);
        return mapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public PutResult put(ProductDto newItem) {
        Product newProduct = mapper.map(newItem, Product.class);
        validator.validate(newProduct);
        try {
            repository.get(newProduct.getId());
            repository.update(newProduct);
            return PutResult.UPDATED;
        } catch (NoSuchElementException e) {
            repository.add(newProduct);
            return PutResult.CREATED;
        }
    }

    @Override
    @CallsLog
    @Transactional
    public void writeOff(int id, int number)
        throws NotEnoughLeftoverException, NoSuchElementException {

        if (number < 0) {
            throw new IllegalArgumentException("number can't be less than 0");
        }
        Product product = repository.get(id);
        checkLeftover(product, number);
        product.setCount(
            product.getCount() - number);
        repository.update(product);
    }

    private void checkLeftover(Product product, int neededCount) throws NotEnoughLeftoverException {
        if (product.getCount() < neededCount) {
            throw new NotEnoughLeftoverException(neededCount, product.getCount());
        }
    }

    private static void updateFields(Product product, ProductUpdateDto updateDto) {
        if (updateDto.getName() != null) {
            product.setName(updateDto.getName());
        }
        if (updateDto.getCount() != null) {
            product.setCount(updateDto.getCount());
        }
        if (updateDto.getPrice() != null) {
            product.setPrice(updateDto.getPrice());
        }
        if (updateDto.getPromotional() != null) {
            product.setPromotional(updateDto.getPromotional());
        }
    }
}
