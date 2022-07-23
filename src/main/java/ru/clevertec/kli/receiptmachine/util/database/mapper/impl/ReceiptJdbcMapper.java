package ru.clevertec.kli.receiptmachine.util.database.mapper.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.util.database.mapper.JdbcMapper;

@Component
public class ReceiptJdbcMapper implements JdbcMapper<Receipt> {

    private static final Logger logger = LogManager.getLogger(ReceiptJdbcMapper.class);

    @Override
    public int prepare(PreparedStatement statement, Receipt item) throws SQLException {
        int index = 0;
        statement.setTimestamp(++index, Timestamp.valueOf(item.getTime()));
        statement.setBigDecimal(++index, item.getSumWithoutCardDiscount());
        statement.setFloat(++index, item.getDiscountCardPercent());
        statement.setBigDecimal(++index, item.getTotalValue());
        if (item.getDiscountCardNumber() != 0) {
            statement.setInt(++index, item.getDiscountCardNumber());
        } else {
            statement.setObject(++index, null);
        }
        logger.trace("PreparedStatement has been filled with an item: {}", item);
        return index;
    }

    @Override
    public Receipt getObject(ResultSet resultSet) throws SQLException {
        Receipt item = Receipt.builder()
            .id(resultSet.getInt("id"))
            .time(resultSet.getTimestamp("time").toLocalDateTime())
            .sumWithoutCardDiscount(resultSet.getBigDecimal("sum_without_card_discount"))
            .discountCardPercent(resultSet.getFloat("discount_card_percent"))
            .totalValue(resultSet.getBigDecimal("total_value"))
            .discountCardNumber(resultSet.getInt("discount_card_number"))
            .build();
        logger.trace("Mapped ResultSet to an item: {}", item);
        return item;
    }
}
