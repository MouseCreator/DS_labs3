package org.example.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider extends AutoCloseable {
    ConnectionWrapper getConnection() throws SQLException;
    void release(Connection connection);
}
