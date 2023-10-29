package org.example.dao;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();
}
