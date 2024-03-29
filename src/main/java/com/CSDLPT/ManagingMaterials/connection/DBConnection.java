package com.CSDLPT.ManagingMaterials.connection;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    final String mssqlSite0 = "MSI";
    final String mssqlSite1 = "MSI\\MSSQLSERVER01";
    final String mssqlSite2 = "MSI\\MSSQLSERVER02";
    final String mssqlSite3 = "MSI\\MSSQLSERVER03";
    final String databaseName = "QLVT_DATHANG";

    public static DBConnection getInstance() {
        return new DBConnection();
    }

    public Connection getConnection(ReqDtoAccount account) throws SQLException {
        String serverDb = switch (account.getBranch().trim().toUpperCase()) {
            case "CN1" -> this.mssqlSite1;
            case "CN2" -> this.mssqlSite2;
            default -> throw new SQLException("Branch not found");
        };

        String connectionString = String.format(
                "jdbc:sqlserver://%s:1433;DatabaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true",
                serverDb,
                this.databaseName,
                account.getUsername(),
                account.getPassword()
        );
        return DriverManager.getConnection(connectionString);
    }
}
