package ru.clevertec.kli.receiptmachine.repository.factory.impl;

import java.util.ArrayList;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.repository.factory.RepositoryFactory;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;

public class LocalRepositoryFactory implements RepositoryFactory {

    @Override
    public Repository<Product> getProductRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Override
    public Repository<DiscountCard> getDiscountCardRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Override
    public Repository<Receipt> getReceiptRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Override
    public Repository<ReceiptPosition> getReceiptPositionRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }
}
