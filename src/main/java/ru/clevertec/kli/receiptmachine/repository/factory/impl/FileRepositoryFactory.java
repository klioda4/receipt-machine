package ru.clevertec.kli.receiptmachine.repository.factory.impl;

import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.repository.factory.RepositoryFactory;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;
import ru.clevertec.kli.receiptmachine.repository.impl.file.FileRepository;
import ru.clevertec.kli.receiptmachine.util.io.input.FileObjectReader;
import ru.clevertec.kli.receiptmachine.util.parse.regex.DiscountCardParser;
import ru.clevertec.kli.receiptmachine.util.parse.regex.ProductParser;

@RequiredArgsConstructor
public class FileRepositoryFactory implements RepositoryFactory {

    private final String productsInputFilename;
    private final String cardsInputFilename;
    private final String invalidInputDataFilename;

    @Override
    public Repository<Product> getProductRepository() {
        try {
            return new FileRepository<>(
                new ArrayList<>(),
                productFileObjectReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Repository<DiscountCard> getDiscountCardRepository() {
        try {
            return new FileRepository<>(
                new ArrayList<>(),
                discountCardFileObjectReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Repository<Receipt> getReceiptRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Override
    public Repository<ReceiptPosition> getReceiptPositionRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }


    private FileObjectReader<DiscountCard> discountCardFileObjectReader() {
        return new FileObjectReader<>(
            cardsInputFilename,
            invalidInputDataFilename,
            new DiscountCardParser());
    }

    private FileObjectReader<Product> productFileObjectReader() {
        return new FileObjectReader<>(
            productsInputFilename,
            invalidInputDataFilename,
            new ProductParser());
    }
}
