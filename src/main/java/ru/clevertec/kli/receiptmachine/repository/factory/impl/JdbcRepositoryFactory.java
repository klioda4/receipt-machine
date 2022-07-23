package ru.clevertec.kli.receiptmachine.repository.factory.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.repository.factory.RepositoryFactory;
import ru.clevertec.kli.receiptmachine.repository.impl.db.JdbcRepository;
import ru.clevertec.kli.receiptmachine.util.database.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.database.mapper.impl.DiscountCardJdbcMapper;
import ru.clevertec.kli.receiptmachine.util.database.mapper.impl.ProductJdbcMapper;
import ru.clevertec.kli.receiptmachine.util.database.mapper.impl.ReceiptJdbcMapper;
import ru.clevertec.kli.receiptmachine.util.database.mapper.impl.ReceiptPositionJdbcMapper;
import ru.clevertec.kli.receiptmachine.util.database.query.impl.DiscountCardSqlQuery;
import ru.clevertec.kli.receiptmachine.util.database.query.impl.ProductSqlQuery;
import ru.clevertec.kli.receiptmachine.util.database.query.impl.ReceiptPositionSqlQuery;
import ru.clevertec.kli.receiptmachine.util.database.query.impl.ReceiptSqlQuery;

@RequiredArgsConstructor
public class JdbcRepositoryFactory implements RepositoryFactory {

    private final DataSource dataSource;

    @Override
    public Repository<Product> getProductRepository() {
        var mapper = new ProductJdbcMapper();
        var query = new ProductSqlQuery();
        return new JdbcRepository<>(dataSource, mapper, query);
    }

    @Override
    public Repository<DiscountCard> getDiscountCardRepository() {
        var mapper = new DiscountCardJdbcMapper();
        var query = new DiscountCardSqlQuery();
        return new JdbcRepository<>(dataSource, mapper, query);
    }

    @Override
    public Repository<Receipt> getReceiptRepository() {
        var mapper = new ReceiptJdbcMapper();
        var query = new ReceiptSqlQuery();
        return new JdbcRepository<>(dataSource, mapper, query);
    }

    @Override
    public Repository<ReceiptPosition> getReceiptPositionRepository() {
        var mapper = new ReceiptPositionJdbcMapper();
        var query = new ReceiptPositionSqlQuery();
        return new JdbcRepository<>(dataSource, mapper, query);
    }
}
