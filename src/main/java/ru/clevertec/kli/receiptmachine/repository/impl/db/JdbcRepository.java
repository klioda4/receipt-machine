package ru.clevertec.kli.receiptmachine.repository.impl.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.exception.RepositoryException;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.Transactional;
import ru.clevertec.kli.receiptmachine.util.database.connection.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.database.mapper.JdbcMapper;
import ru.clevertec.kli.receiptmachine.util.database.query.Query;

@RequiredArgsConstructor
@Transactional
public class JdbcRepository<T extends Entity> implements Repository<T> {

    private static final Logger logger = LogManager.getLogger(JdbcRepository.class);

    private final DataSource dataSource;
    private final JdbcMapper<T> jdbcMapper;
    private final Query<T> query;

    @Override
    public void add(T item) throws RepositoryException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.getCreateQuery(),
                Statement.RETURN_GENERATED_KEYS)) {

            jdbcMapper.prepare(statement, item);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new RepositoryException("item " + item + " has not added");
            }
            item.setId(obtainGeneratedId(statement));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public T get(int id) throws NoSuchElementException, RepositoryException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.getReadQuery())) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return jdbcMapper.getObject(resultSet);
            } else {
                String message = String.format("item for id=%d not found", id);
                logger.debug(message);
                throw new NoSuchElementException(message);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<T> getAll() throws RepositoryException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query.getReadAllQuery());
            List<T> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(jdbcMapper.getObject(resultSet));
            }
            return items;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(T item) throws NoSuchElementException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.getUpdateQuery())) {

            int fieldsNumber = jdbcMapper.prepare(statement, item);
            statement.setInt(fieldsNumber + 1, item.getId());

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new NoSuchElementException(
                    "No items have been updated by id=" + item.getId());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void remove(T item) throws RepositoryException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.getDeleteQuery())) {

            statement.setInt(1, item.getId());
            int updatedRows = statement.executeUpdate();
            logger.debug("item with id={} {}", item.getId(),
                updatedRows != 0 ? "has been deleted" : "not found");
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private static int obtainGeneratedId(Statement statement) throws SQLException,
        RepositoryException {
        try (ResultSet generatedId = statement.getGeneratedKeys()) {
            if (generatedId.next()) {
                return generatedId.getInt(1);
            } else {
                throw new RepositoryException("id has not been obtained");
            }
        }
    }
}
