package ru.clevertec.kli.receiptmachine.util.database.query.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.util.database.query.Query;

@Component
@RequiredArgsConstructor
public class DiscountCardSqlQuery implements Query<DiscountCard> {

    private static final String CREATE_QUERY =
        "INSERT INTO discount_cards (number, discount)"
            + " VALUES (?, ?);";
    private static final String READ_QUERY =
        "SELECT number, discount"
            + " FROM discount_cards"
            + " WHERE number=?;";
    private static final String UPDATE_QUERY =
        "UPDATE discount_cards"
            + " SET number=?, discount=?"
            + " WHERE number=?;";
    private static final String DELETE_QUERY =
        "DELETE FROM discount_cards"
            + " WHERE number=?;";
    private static final String READ_ALL_QUERY =
        "SELECT number, discount"
            + " FROM discount_cards;";

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
