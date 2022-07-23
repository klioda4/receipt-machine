package ru.clevertec.kli.receiptmachine.util.database.mapper.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.util.database.mapper.JdbcMapper;

@Component
public class DiscountCardJdbcMapper implements JdbcMapper<DiscountCard> {

    private static final Logger logger = LogManager.getLogger(DiscountCardJdbcMapper.class);

    @Override
    public int prepare(PreparedStatement statement, DiscountCard item) throws SQLException {
        int index = 0;
        statement.setInt(++index, item.getNumber());
        statement.setFloat(++index, item.getDiscount());
        logger.trace("PreparedStatement has been filled with an item: {}", item);
        return index;
    }

    @Override
    public DiscountCard getObject(ResultSet resultSet) throws SQLException {
        DiscountCard item = DiscountCard.builder()
            .number(resultSet.getInt("number"))
            .discount(resultSet.getFloat("discount"))
            .build();
        logger.trace("Mapped ResultSet to an item: {}", item);
        return item;
    }
}
