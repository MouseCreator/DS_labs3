package org.example.util;

import java.sql.Connection;

/**
 * Provides Connection to Data Bases
 * Returns connection to Connection Provider on release
 */
public class ConnectionWrapper implements AutoCloseable {
    private final ConnectionProvider provider;
    private final Connection connection;
    public ConnectionWrapper(ConnectionProvider pool, Connection connection) {
        this.provider = pool;
        this.connection = connection;
    }
    public Connection get() {
        return connection;
    }
    @Override
    public void close() {
        provider.release(connection);
    }
}
