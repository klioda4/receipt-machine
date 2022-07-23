package ru.clevertec.kli.receiptmachine.util.database.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcMapper<T> {

    int prepare(PreparedStatement statement, T item) throws SQLException;

    T getObject(ResultSet resultSet) throws SQLException;
}
