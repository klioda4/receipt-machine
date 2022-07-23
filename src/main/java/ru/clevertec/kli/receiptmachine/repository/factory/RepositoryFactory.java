package ru.clevertec.kli.receiptmachine.repository.factory;

import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;

/**
 * Factory for study
 */
public interface RepositoryFactory {

    Repository<Product> getProductRepository();

    Repository<DiscountCard> getDiscountCardRepository();

    Repository<Receipt> getReceiptRepository();

    Repository<ReceiptPosition> getReceiptPositionRepository();
}