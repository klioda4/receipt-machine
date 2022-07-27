package ru.clevertec.kli.receiptmachine.util.database.transaction.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.util.database.transaction.TransactionManager;

public class SimpleTransactionManager implements TransactionManager {

    private static final Logger logger = LogManager.getLogger(SimpleTransactionManager.class);

    private final Connection connection;
    private final Deque<Savepoint> pointsStack = new ArrayDeque<>();

    public SimpleTransactionManager(Connection connection) {
        this.connection = connection;
        logger.trace("SimpleTransactionManager created");
    }

    @Override
    public boolean isTransactionPresent() {
        return !pointsStack.isEmpty();
    }

    @Override
    public Savepoint begin() throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint();
        if (pointsStack.isEmpty()) {
            logger.debug("Starting transaction with point {}", savepoint);
        } else {
            logger.trace("Adding savepoint to current transaction: {}", savepoint);
        }
        pointsStack.add(savepoint);
        return savepoint;
    }

    @Override
    public void finish(Savepoint savepoint) throws SQLException {
        if (savepoint != pointsStack.getFirst()) {
            checkSavepoint(savepoint);
            logger.trace("Releasing savepoint {}", savepoint);
            connection.releaseSavepoint(savepoint);
            removeFromStack(savepoint);
        } else {
            logger.debug("Committing transaction with savepoint {} (autocommit is {})", savepoint, connection.getAutoCommit());
            connection.commit();
            finishTransaction();
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        checkSavepoint(savepoint);
        logger.debug("Rollback to savepoint {}", savepoint);
        connection.rollback(savepoint);
        if (savepoint == pointsStack.getFirst()) {
            finishTransaction();
        }
    }

    private void finishTransaction() throws SQLException {
        pointsStack.clear();
        connection.setAutoCommit(true);
        logger.debug("Transaction has finished");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void removeFromStack(Savepoint savepoint) {
        while (pointsStack.removeLast() != savepoint)
            ;
    }

    private void checkSavepoint(Savepoint savepoint) throws IllegalArgumentException {
        if (!pointsStack.contains(savepoint)) {
            throw new IllegalArgumentException("Savepoints received not by current object or "
                + "inside already finished blocks can't be used here.");
        }
    }
}
