package ru.clevertec.kli.receiptmachine.util.database.transaction.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.util.database.connection.pool.ConnectionPool;
import ru.clevertec.kli.receiptmachine.util.database.connection.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.database.transaction.TransactionManager;

@RequiredArgsConstructor
public class TransactionalDataSource implements DataSource, TransactionManager, AutoCloseable {

    private static final Logger logger = LogManager.getLogger(TransactionalDataSource.class);

    private final ConnectionPool connectionPool;

    private final Map<Thread, Entry<Connection, TransactionManager>> currentTransactions =
        new ConcurrentHashMap<>();

    /**
     * @throws IllegalStateException if thread was interrupted while waiting for connection
     */
    @Override
    public Connection getConnection() throws SQLException {
        logger.trace("Giving connection");
        Entry<Connection, TransactionManager> entry = currentTransactions.get(
            Thread.currentThread());
        if (entry == null) {
            entry = putNewEntry();
        }
        return entry.getKey();
    }

    @Override
    public boolean isTransactionPresent() throws SQLException {
        TransactionManager transactionManager = getCurrentEntryOrThrowException()
            .getValue();
        return transactionManager.isTransactionPresent();
    }

    @Override
    public Savepoint begin() throws SQLException {
        Entry<Connection, TransactionManager> entry = currentTransactions.get(
            Thread.currentThread());
        if (entry == null) {
            entry = putNewEntry();
        }
        TransactionManager transactionManager = entry.getValue();
        return transactionManager.begin();
    }

    @Override
    public void finish(Savepoint savepoint) throws SQLException {
        Entry<Connection, TransactionManager> entry = getCurrentEntryOrThrowException();
        TransactionManager transactionManager = entry.getValue();
        transactionManager.finish(savepoint);
        removeIfFinished(entry);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        Entry<Connection, TransactionManager> entry = getCurrentEntryOrThrowException();
        TransactionManager transactionManager = entry.getValue();
        transactionManager.rollback(savepoint);
        removeIfFinished(entry);
    }

    @Override
    public void close() {
        currentTransactions.forEach(
            (thread, conn) -> releaseConnectionIgnoreException(conn.getKey()));
        currentTransactions.clear();
    }

    private Entry<Connection, TransactionManager> putNewEntry()
        throws SQLException, IllegalStateException {

        Connection connection = getNewConnection();
        Entry<Connection, TransactionManager> newEntry =
            new SimpleEntry<>(connection, new SimpleTransactionManager(connection));
        currentTransactions.put(Thread.currentThread(), newEntry);
        return newEntry;
    }

    private Entry<Connection, TransactionManager> getCurrentEntryOrThrowException() {
        Entry<Connection, TransactionManager> entry = currentTransactions.get(
            Thread.currentThread());
        if (entry == null) {
            throw new IllegalStateException("No current transaction");
        } else {
            return entry;
        }
    }

    private void removeIfFinished(Entry<Connection, TransactionManager> entry) throws SQLException {
        if (entry.getValue().isTransactionPresent()) {
            return;
        }
        connectionPool.releaseConnection(entry.getKey());
        currentTransactions.remove(Thread.currentThread());
    }

    private void releaseConnectionIgnoreException(Connection connection) {
        try {
            connectionPool.releaseConnection(connection);
        } catch (SQLException ignored) {
        }
    }

    private Connection getNewConnection() throws SQLException {
        try {
            return connectionPool.getConnection();
        } catch (InterruptedException e) {
            logger.warn("Interrupted");
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread has been interrupted", e);
        }
    }
}
