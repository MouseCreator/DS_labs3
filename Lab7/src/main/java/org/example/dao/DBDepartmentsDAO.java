package org.example.dao;

import org.example.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DBDepartmentsDAO implements DepartmentsDAO {
    private final String tableName;
    private ConnectionPool connectionPool;

    public DBDepartmentsDAO(String tableName) {
        this.tableName = tableName;
    }

    public List<Department> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);
        List<Department> resultList = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                Long id = set.getLong("id");
            }
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public Department create(Department object) {
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties dbProperties = new Properties();
            dbProperties.put("user", "root");
            dbProperties.put("password", "password");
            Connection connection = DriverManager.getConnection("jdbc://mysql://localhost:3306/departments", dbProperties);

            String sql = "SELECT * FROM department WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                Department department = new Department();
                department.setId(set.getObject("id", Long.class));
                department.setName(set.getString("name"));
                return Optional.of(department);
            }
            return Optional.empty();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load JDBC Driver", e);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to DB", e);
        }
    }
}
