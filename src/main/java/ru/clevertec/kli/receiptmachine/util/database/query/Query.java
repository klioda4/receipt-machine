package ru.clevertec.kli.receiptmachine.util.database.query;

public interface Query<T> {

    String getCreateQuery();

    String getReadQuery();

    String getUpdateQuery();

    String getDeleteQuery();

    String getReadAllQuery();
}
