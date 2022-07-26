package ru.clevertec.kli.receiptmachine.util.database.connection.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleDataSource implements DataSource {

    private final String url;
    private final Properties connectionInfo;

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, connectionInfo);
    }
}
