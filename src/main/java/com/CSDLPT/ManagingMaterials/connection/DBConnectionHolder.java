package com.CSDLPT.ManagingMaterials.connection;

import com.CSDLPT.ManagingMaterials.model.Account;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionHolder {
    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    @Value("${mssql.server.site_1}")
    private String mssqlSite1;
    @Value("${mssql.server.site_2}")
    private String mssqlSite2;
    @Value("${mssql.database.name}")
    private String databaseName;

    public void buildConnection(Account account) throws SQLException {
        String serverDb = switch (account.getBranch().trim().toUpperCase()) {
            case "CN1" -> this.mssqlSite1;
            case "CN2" -> this.mssqlSite2;
            default -> throw new SQLException("Branch not found");
        };
        System.out.println(mssqlSite1);
        System.out.println(mssqlSite2);
        System.out.println(databaseName);

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