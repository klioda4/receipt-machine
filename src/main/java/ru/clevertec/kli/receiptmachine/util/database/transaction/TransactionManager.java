package ru.clevertec.kli.receiptmachine.util.database.transaction;

import java.sql.SQLException;
import java.sql.Savepoint;

public interface TransactionManager {

    /**
     * @return true if a transaction is in progress
     */
    boolean isTransactionPresent() throws SQLException;

    /**
     * Start transaction or add savepoint if transaction is in progress
     * @return Savepoint to manage transaction with
     */
    Savepoint begin() throws SQLException;

    /**
     * Finish transaction if first savepoint provided, continues transaction otherwise
     * @param savepoint Object, received by begin() method
     */
    void finish(Savepoint savepoint) throws SQLException;

    /**
     * Rollbacks to specified savepoint. If it's savepoint of transaction start then finishes
     * transaction
     * @param savepoint Object, received by begin() method
     */
    void rollback(Savepoint savepoint) throws SQLException;
}
