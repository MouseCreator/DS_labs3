package org.example.dao;

import org.example.util.ConnectionPool;
import org.example.util.ConnectionWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBCrudDao<T extends IdIterable> implements GenericCrudDao<T> {

    protected final ConnectionPool connectionPool;
    protected abstract String getTableName();
    protected String insertSQL() {
        String sql = query("INSERT INTO %s ");
        StringBuilder builder = new StringBuilder(sql);
        builder.append("(");
        String[] fields = fields();
        for (int i = 1; i < fields.length; ++i) {
            builder.append(fields[i]);
            if (i != fields.length -1) {
                builder.append(" ");
            }
        }
        builder.append(") VALUES (");
        for (int i = 1; i < fields.length; ++i) {
            builder.append("?");
            if (i != fields.length -1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
    protected String updateSQL() {
        String sql = query("UPDATE %s SET ");
        StringBuilder builder = new StringBuilder(sql);
        String[] fields = fields();
        for (int i = 1; i < fields.length; ++i) {
            builder.append(fields[i]).append(" = ?");
            if (i != fields.length -1) {
                builder.append(", ");
            }
        }
        builder.append(String.format(" WHERE %s = ?", fields[0]));
        return builder.toString();
    }
    protected abstract String[] fields();
    protected abstract int applyCreation(PreparedStatement statement, T object) throws SQLException;
    public AbstractDBCrudDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<T> findAll() {
        String sql = query("SELECT * FROM %s");
        List<T> resultList = new ArrayList<>();
        try(ConnectionWrapper connection = connectionPool.getConnection()) {
            Statement statement = connection.get().createStatement();
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultList.add(toInstance(set));
            }
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
    private String query(String q) {
        return String.format(q, getTableName());
    }
    @Override
    public T create(T object) {
        String sql = insertSQL();
        try (ConnectionWrapper wrapper = connectionPool.getConnection()) {
            try(PreparedStatement statement = wrapper.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                applyCreation(statement, object);
                int affectedRows = statement.executeUpdate();
                if (affectedRows < 1) {
                    throw new RuntimeException("Expected to add at least one row");
                }
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getObject(1, Long.class);
                    object.setId(id);
                }
                return object;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(T object) {
        String sql = updateSQL();
        try (ConnectionWrapper wrapper = connectionPool.getConnection()) {
            try (PreparedStatement statement = wrapper.get().prepareStatement(sql)) {
                int fields = applyCreation(statement, object);
                statement.setLong(fields+1, object.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows < 1) {
                    throw new RuntimeException("No department with the specified ID found for update.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(T department) {
        delete(department.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = query("DELETE FROM %s WHERE id = ?");
        try (ConnectionWrapper wrapper = connectionPool.getConnection()) {
            try (PreparedStatement statement = wrapper.get().prepareStatement(sql)) {
                statement.setLong(1, id);
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> find(Long id) {
        try (ConnectionWrapper connection = connectionPool.getConnection()) {
            String sql = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
            try (PreparedStatement statement = connection.get().prepareStatement(sql)) {
                statement.setLong(1, id);
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return Optional.of(toInstance(set));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to DB", e);
        }
    }

    protected abstract T toInstance(ResultSet set) throws SQLException;
}
