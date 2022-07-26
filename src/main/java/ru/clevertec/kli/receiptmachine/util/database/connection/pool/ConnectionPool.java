package ru.clevertec.kli.receiptmachine.util.database.connection.pool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool extends AutoCloseable {

    Connection getConnection() throws InterruptedException, SQLException;

    void releaseConnection(Connection connection) throws SQLException;
}
