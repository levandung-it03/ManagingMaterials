package com.CSDLPT.ManagingMaterials.connection;

import com.CSDLPT.ManagingMaterials.model.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionHolder {
    private final String mssqlSite1 = "MSI\\MSSQLSERVER01";
    private final String mssqlSite2 = "MSI\\MSSQLSERVER02";
    private final String databaseName = "QLVT_DATHANG";
    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public void buildConnection(Account account) throws SQLException {
        String serverDb = switch (account.getBranch().trim().toUpperCase()) {
            case "CN1" -> this.mssqlSite1;
            case "CN2" -> this.mssqlSite2;
            default -> throw new SQLException("Branch not found");
        };

        String connectionString = String.format(
            "jdbc:sqlserver://%s;DatabaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true",
            serverDb,
            this.databaseName,
            account.getUsername().trim().toUpperCase(),
            account.getPassword().trim()
        );

        //--May throw SQLException.
        this.setConnection(DriverManager.getConnection(connectionString));
    }

    public Connection getConnection() {
        return threadLocal.get();
    }

    public void setConnection(Connection connection) {
        threadLocal.set(connection);
    }

    public void removeConnection() throws SQLException {
        this.getConnection().close();
        threadLocal.remove();
    }
}