package org.example.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBDepartmentsDAO<T> {
    private final String tableName;
    private ConnectionPool connectionPool;

    public DBDepartmentsDAO(String tableName) {
        this.tableName = tableName;
    }

    public List<T> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);
        List<T> resultList = new ArrayList<>();
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
}
