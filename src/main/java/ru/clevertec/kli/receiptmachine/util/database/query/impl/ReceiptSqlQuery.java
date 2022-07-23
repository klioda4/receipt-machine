package ru.clevertec.kli.receiptmachine.util.database.query.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.util.database.query.Query;

@Component
@RequiredArgsConstructor
public class ReceiptSqlQuery implements Query<Receipt> {

    private static final String CREATE_QUERY =
        "INSERT INTO receipts (time, sum_without_card_discount, discount_card_percent, total_value, discount_card_number)"
            + " VALUES (?, ?, ?, ?, ?);";
    private static final String READ_QUERY =
        "SELECT id, time, sum_without_card_discount, discount_card_percent, total_value, discount_card_number"
            + " FROM receipts"
            + " WHERE id=?;";
    private static final String UPDATE_QUERY =
        "UPDATE receipts"
            + " SET time=?, sum_without_card_discount=?, discount_card_percent=?, total_value=?, discount_card_number=?"
            + " WHERE id=?;";
    private static final String DELETE_QUERY =
        "DELETE FROM receipts"
            + " WHERE id=?;";
    private static final String READ_ALL_QUERY =
        "SELECT id, time, sum_without_card_discount, discount_card_percent, total_value, discount_card_number"
            + " FROM receipts;";

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
