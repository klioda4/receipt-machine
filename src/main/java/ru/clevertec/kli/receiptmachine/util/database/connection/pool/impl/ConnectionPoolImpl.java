package ru.clevertec.kli.receiptmachine.util.database.connection.pool.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.util.database.connection.ManagedConnectionProxy;
import ru.clevertec.kli.receiptmachine.util.database.connection.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.database.connection.pool.ConnectionPool;

public class ConnectionPoolImpl implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(ConnectionPoolImpl.class);

    private final DataSource dataSource;
    private final BlockingQueue<Connection> freeConnections;
    private final BlockingQueue<Connection> inUseConnections;

    private final int maxConnectionsNumber;
    private volatile int currentConnectionsNumber = 0;

    public ConnectionPoolImpl(DataSource dataSource, int maxConnectionsNumber) {
        this.dataSource = dataSource;
        this.maxConnectionsNumber = maxConnectionsNumber;
        freeConnections = new ArrayBlockingQueue<>(maxConnectionsNumber);
        inUseConnections = new ArrayBlockingQueue<>(maxConnectionsNumber);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = freeConnections.poll();
        if (connection == null) {
            tryToCreateConnection();
            connection = takeFreeConnection();
        }
        inUseConnections.add(connection);
        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(true);
        if (inUseConnections.remove(connection)) {
            freeConnections.add(connection);
        }
    }

    @Override
    public void close() {
        freeConnections.forEach(this::closeConnectionIgnoreException);
        inUseConnections.forEach(this::closeConnectionIgnoreException);
    }

    private Connection takeFreeConnection() {
        try {
            return freeConnections.take();
        } catch (InterruptedException e) {
            logger.warn("Interrupted");
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread has been interrupted", e);
        }
    }

    private void tryToCreateConnection() throws SQLException {
        synchronized (this) {
            if (currentConnectionsNumber < maxConnectionsNumber) {
                ++currentConnectionsNumber;
            } else {
                return;
            }
        }
        freeConnections.add(createConnection());
    }

    private void closeConnectionIgnoreException(Connection connection) {
        try {
            ((ManagedConnectionProxy) connection).realClose();
        } catch (SQLException ignored) {
        }
    }

    private Connection createConnection() throws SQLException {
        return new ManagedConnectionProxy(
            dataSource.getConnection());
    }
}
