package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ResDtoEmployeeInfo;
import com.CSDLPT.ManagingMaterials.model.Account;
import com.CSDLPT.ManagingMaterials.model.Enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    public ResDtoEmployeeInfo authenticate(Account account) throws SQLException, NoSuchElementException {
        //--Connection from authenticated User.
        DBConnectionHolder connectionHolder = new DBConnectionHolder();
        connectionHolder.buildConnection(account);

        CallableStatement statement = connectionHolder.getConnection().prepareCall("{call SP_GET_USER_INFO_BY_LOGIN(?)}");
        statement.setString(1, account.getUsername());

        ResultSet resultSet = statement.executeQuery();
        ResDtoEmployeeInfo employeeInfo;

        if (resultSet.next()) {
            employeeInfo = ResDtoEmployeeInfo.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .branch(account.getBranch())
                .employeeId(resultSet.getString("MANV"))
                .fullName(resultSet.getString("HOTEN"))
                .role(Role.valueOf(resultSet.getString("TENNHOM")))
                .build();
        } else {
            throw new NoSuchElementException("Employee Not Found");
        }

        //--Close Connection from authenticated User.
        connectionHolder.removeConnection();

        return employeeInfo;
    }
}
