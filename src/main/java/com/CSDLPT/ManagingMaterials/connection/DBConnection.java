package com.CSDLPT.ManagingMaterials.connection;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    @Value("${mssql_server.name}")
    public static String serverName;
    @Value("${mssql_database.name}")
    public static String databaseName;

    public static Connection getConnection(ReqDtoAccount account) throws SQLException {
        String serverDb = switch (account.getBranch().trim().toUpperCase()) {
            case "CN1" -> serverName + "1";
            case "CN2" -> serverName + "2";
            default -> throw new SQLException("Branch not found");
        };

        String connectionString = String.format(
                "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s",
                databaseName,
                serverDb,
                account.getUsername(),
                account.getPassword()
        );
        return DriverManager.getConnection(connectionString);
    }
}
