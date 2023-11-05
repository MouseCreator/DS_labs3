package org.example.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool implements ConnectionProvider {
    private final int size;
    private final List<Connection> connectionPool;
    private ConnectionPool(int size) {
        this.size = size;
        connectionPool = new ArrayList<>(size);
    }

    @Override
    public ConnectionWrapper getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            throw new SQLException("Connection pool is full");
        }
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        return new ConnectionWrapper(this, connection);
    }
    @Override
    public void close() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connectionPool.clear();
    }

    @Override
    public void release(Connection connection) {
        if (connection == null)
            return;
        connectionPool.add(connection);
    }

    private void initialize() {
        assert connectionPool.size() == 0;
        for (int i = 0; i < size; ++i) {
            connectionPool.add(ConnectionUtil.getConnection());
        }
    }

    public static ConnectionPool commonPool(int size) {
        ConnectionPool pool = new ConnectionPool(size);
        pool.initialize();
        return pool;
    }
}
