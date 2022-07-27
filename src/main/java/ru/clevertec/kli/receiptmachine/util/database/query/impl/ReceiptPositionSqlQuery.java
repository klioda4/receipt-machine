package ru.clevertec.kli.receiptmachine.util.database.query.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.util.database.query.Query;

@Component
@RequiredArgsConstructor
public class ReceiptPositionSqlQuery implements Query<ReceiptPosition> {

    private static final String CREATE_QUERY =
        "INSERT INTO receipt_positions (quantity, product_discount_percent, product_price, value_without_discount, value_with_discount, product_id, receipt_id)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String READ_QUERY =
        "SELECT id, quantity, product_discount_percent, product_price, value_without_discount, value_with_discount, product_id, receipt_id"
            + " FROM receipt_positions"
            + " WHERE id=?;";
    private static final String UPDATE_QUERY =
        "UPDATE receipt_positions"
            + " SET quantity=?, product_discount_percent=?, product_price=?, value_without_discount=?, value_with_discount=?, product_id=?, receipt_id=?"
            + " WHERE id=?;";
    private static final String DELETE_QUERY =
        "DELETE FROM receipt_positions"
            + " WHERE id=?;";
    private static final String READ_ALL_QUERY =
        "SELECT id, quantity, product_discount_percent, product_price, value_without_discount, value_with_discount, product_id, receipt_id"
            + " FROM receipt_positions;";

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
