package ru.clevertec.kli.receiptmachine.util.database.mapper.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.util.database.mapper.JdbcMapper;

@Component
public class ReceiptPositionJdbcMapper implements JdbcMapper<ReceiptPosition> {

    private static final Logger logger = LogManager.getLogger(ReceiptPositionJdbcMapper.class);

    @Override
    public int prepare(PreparedStatement statement, ReceiptPosition item) throws SQLException {
        int index = 0;
        statement.setInt(++index, item.getQuantity());
        statement.setFloat(++index, item.getProductDiscountPercent());
        statement.setBigDecimal(++index, item.getProductPrice());
        statement.setBigDecimal(++index, item.getValueWithoutDiscount());
        statement.setBigDecimal(++index, item.getValueWithDiscount());
        statement.setInt(++index, item.getProductId());
        statement.setInt(++index, item.getReceiptId());
        logger.trace("PreparedStatement has been filled with an item: {}", item);
        return index;
    }

    @Override
    public ReceiptPosition getObject(ResultSet resultSet) throws SQLException {
        ReceiptPosition item = ReceiptPosition.builder()
            .id(resultSet.getInt("id"))
            .quantity(resultSet.getInt("quantity"))
            .productDiscountPercent(resultSet.getFloat("product_discount_percent"))
            .productPrice(resultSet.getBigDecimal("product_price"))
            .valueWithoutDiscount(resultSet.getBigDecimal("value_without_discount"))
            .valueWithDiscount(resultSet.getBigDecimal("value_with_discount"))
            .productId(resultSet.getInt("product_id"))
            .receiptId(resultSet.getInt("receipt_id"))
            .build();
        logger.trace("Mapped ResultSet to an item: {}", item);
        return item;
    }
}
