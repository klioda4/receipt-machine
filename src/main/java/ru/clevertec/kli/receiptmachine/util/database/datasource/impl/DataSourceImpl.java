package ru.clevertec.kli.receiptmachine.util.database.datasource.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.database.datasource.DataSource;

@RequiredArgsConstructor
public class DataSourceImpl implements DataSource {

//    private final String user;
//    private final String password;
//
//    public DataSourceImpl(String connectionString, String user, String password) {
//        this.connectionString = connectionString;
//        this.user = user;
//        this.password = password;
//    }

    private final String url;
    private final Properties connectionInfo;

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, connectionInfo);
    }
}
