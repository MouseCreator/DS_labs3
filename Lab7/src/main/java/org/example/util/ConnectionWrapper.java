package org.example.util;

import java.sql.Connection;

public class ConnectionWrapper implements AutoCloseable {
    private final ConnectionProvider pool;
    private final Connection connection;
    public ConnectionWrapper(ConnectionProvider pool, Connection connection) {
        this.pool = pool;
        this.connection = connection;
    }
    public Connection get() {
        return connection;
    }
    @Override
    public void close() {
        pool.release(connection);
    }
}
