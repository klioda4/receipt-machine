package ru.clevertec.kli.receiptmachine.util.database.mapper.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.util.database.mapper.JdbcMapper;

@Component
public class ProductJdbcMapper implements JdbcMapper<Product> {

    private static final Logger logger = LogManager.getLogger(ProductJdbcMapper.class);

    @Override
    public int prepare(PreparedStatement statement, Product item) throws SQLException {
        int index = 0;
        statement.setString(++index, item.getName());
        statement.setBigDecimal(++index, item.getPrice());
        statement.setBoolean(++index, item.isPromotional());
        statement.setInt(++index, item.getCount());
        logger.trace("PreparedStatement has been filled with an item: {}", item);
        return index;
    }

    @Override
    public Product getObject(ResultSet resultSet) throws SQLException {
        Product item = Product.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("name"))
            .price(resultSet.getBigDecimal("price"))
            .promotional(resultSet.getBoolean("promotional"))
            .count(resultSet.getInt("quantity"))
            .build();
        logger.trace("Mapped ResultSet to an item: {}", item);
        return item;
    }
}
