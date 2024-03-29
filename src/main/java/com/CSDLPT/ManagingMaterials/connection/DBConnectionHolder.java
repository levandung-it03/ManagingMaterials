package com.CSDLPT.ManagingMaterials.connection;

import java.sql.Connection;

public class DBConnectionHolder {
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConnection() {
        return threadLocal.get();
    }

    public static void setConnection(Connection connection) {
        threadLocal.set(connection);
    }

    public static void removeConnection() {
        threadLocal.remove();
    }
}