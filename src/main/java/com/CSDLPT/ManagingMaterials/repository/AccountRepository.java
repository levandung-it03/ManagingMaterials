package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnection;
import com.CSDLPT.ManagingMaterials.dto.ResDtoEmployeeInfo;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;
import com.CSDLPT.ManagingMaterials.model.Enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    public ResDtoEmployeeInfo authenticate(ReqDtoAccount account) throws SQLException, NoSuchElementException {
        //--Connection from authenticated User.
        Connection connection = DBConnection.getInstance().getConnection(account);

        CallableStatement statement = connection.prepareCall("{call SP_LayThongTin(?)}");
        statement.setString(1, account.getUsername());
        ResultSet resultSet = statement.executeQuery();
        connection.close();

        if (resultSet.next()) {
            return ResDtoEmployeeInfo.builder()
                    .employeeId(resultSet.getString(""))
                    .fullName(resultSet.getString(""))
                    .role(Role.valueOf(resultSet.getString("")))
                    .build();
        } else {
            throw new NoSuchElementException("Employee Not Found");
        }
    }
}
