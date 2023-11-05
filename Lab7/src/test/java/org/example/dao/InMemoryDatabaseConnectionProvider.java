package org.example.dao;

import org.example.util.ConnectionProvider;
import org.example.util.ConnectionWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class InMemoryDatabaseConnectionProvider implements ConnectionProvider {

    private Connection connection;

    public void createDB() {
        try {
            Class.forName("org.h2.Driver");
            Properties properties = generateProperties();
            connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", properties);
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE departments (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
                statement.execute("""
                       CREATE TABLE employees (
                                                  id BIGINT AUTO_INCREMENT NOT NULL,
                                                  name VARCHAR(255),
                                                  age INT,
                                                  role VARCHAR(255),
                                                  experience INT,
                                                  department BIGINT NOT NULL,
                                                  PRIMARY KEY (id)
                                              );""");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Properties generateProperties() {
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");
        properties.put("dialect", "h2");
        return properties;
    }

    public void tearDown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE departments");
            stmt.execute("DROP TABLE employees");
        }
        connection.close();
    }

    @Override
    public ConnectionWrapper getConnection() {
        return new ConnectionWrapper(this, connection);
    }

    @Override
    public void release(Connection connection) {

    }

    @Override
    public void close() throws Exception {
        tearDown();
    }
}
