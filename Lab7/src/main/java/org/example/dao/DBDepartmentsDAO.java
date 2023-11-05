package org.example.dao;

import org.example.model.Department;
import org.example.util.ConnectionPool;
import org.example.util.ConnectionWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBDepartmentsDAO implements DepartmentsDAO {
    private final ConnectionPool connectionPool;
    private final String tableName = "departments";
    public DBDepartmentsDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Department> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);
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

    @Override
    public Department create(Department department) {
        return null;
    }

    @Override
    public void update(Department object) {

    }

    @Override
    public void delete(Department object) {

    }

    @Override
    public boolean delete(Long id) {
        return false;
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
