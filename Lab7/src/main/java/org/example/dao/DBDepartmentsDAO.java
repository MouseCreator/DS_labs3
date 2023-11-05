package org.example.dao;

import org.example.model.Department;
import org.example.util.ConnectionProvider;
import org.example.util.ConnectionWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBDepartmentsDAO implements DepartmentsDAO {
    private final ConnectionProvider connectionPool;
    private final String tableName = "departments";
    public DBDepartmentsDAO(ConnectionProvider provider) {
        this.connectionPool = provider;
    }
    public List<Department> findAll() {
        String sql = query("SELECT * FROM %s");
        List<Department> resultList = new ArrayList<>();
        try(ConnectionWrapper connection = connectionPool.getConnection()) {
            Statement statement = connection.get().createStatement();
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultList.add(toDepartment(set));
            }
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
    private String query(String q) {
        return String.format(q, tableName);
    }
    @Override
    public Department create(Department department) {
       String sql = query("INSERT INTO %s (name) VALUES (?)");
        try (ConnectionWrapper wrapper = connectionPool.getConnection()) {
            try(PreparedStatement statement = wrapper.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, department.getName());
                int affectedRows =  statement.executeUpdate();
                if (affectedRows < 1) {
                    throw new RuntimeException("Expected to add at least one row");
                }
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getObject(1, Long.class);
                    department.setId(id);
                }
                return department;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Department department) {
        String sql = query("UPDATE %s SET name = ? WHERE id = ?");
        try (ConnectionWrapper wrapper = connectionPool.getConnection()) {
            try (PreparedStatement statement = wrapper.get().prepareStatement(sql)) {
                statement.setString(1, department.getName());
                statement.setLong(2, department.getId());
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
    public void delete(Department department) {
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
    public Optional<Department> find(Long id) {
        try (ConnectionWrapper connection = connectionPool.getConnection()) {
            String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
            try (PreparedStatement statement = connection.get().prepareStatement(sql)) {
                statement.setLong(1, id);
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return Optional.of(toDepartment(set));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to DB", e);
        }
    }

    private Department toDepartment(ResultSet set) throws SQLException {
        Department department = new Department();
        department.setId(set.getObject("id", Long.class));
        department.setName(set.getString("name"));
        return department;
    }
}
