package ru.clevertec.kli.receiptmachine.util.database.query.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.util.database.query.Query;

@Component
@RequiredArgsConstructor
public class ProductSqlQuery implements Query<Product> {

    private static final String CREATE_QUERY =
        "INSERT INTO products (name, price, promotional, quantity)"
            + " VALUES (?, ?, ?, ?);";
    private static final String READ_QUERY =
        "SELECT id, name, price, promotional, quantity"
            + " FROM products"
            + " WHERE id=?;";
    private static final String UPDATE_QUERY =
        "UPDATE products"
            + " SET name=?, price=?, promotional=?, quantity=?"
            + " WHERE id=?;";
    private static final String DELETE_QUERY =
        "DELETE FROM products"
            + " WHERE id=?;";
    private static final String READ_ALL_QUERY =
        "SELECT id, name, price, promotional, quantity"
            + " FROM products;";

    @Override
    public String getCreateQuery() {
        return CREATE_QUERY;
    }

    @Override
    public String getReadQuery() {
        return READ_QUERY;
    }

    @Override
    public String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    public String getDeleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    public String getReadAllQuery() {
        return READ_ALL_QUERY;
    }
}
