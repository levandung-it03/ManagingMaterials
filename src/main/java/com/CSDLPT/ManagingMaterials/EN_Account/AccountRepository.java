package com.CSDLPT.ManagingMaterials.EN_Account;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ReqDtoAddingAccount;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final DBConnectionHolder connectionHolder;
    private final Logger logger;

    public ResDtoUserInfo authenticate(Account account) throws SQLException, NoSuchElementException {
        //--Connection from authenticated User.
        connectionHolder.buildConnection(account);

        CallableStatement statement = connectionHolder.getConnection().prepareCall("{call SP_GET_USER_INFO_BY_LOGIN(?)}");
        statement.setString(1, account.getUsername());

        ResultSet resultSet = statement.executeQuery();
        ResDtoUserInfo userInfo;

        if (resultSet.next()) {
            userInfo = ResDtoUserInfo.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .branch(account.getBranch())
                .employeeId(resultSet.getString("MANV"))
                .fullName(resultSet.getString("HOTEN"))
                .role(RoleEnum.valueOf(resultSet.getString("TENNHOM")))
                .build();
        } else {
            throw new NoSuchElementException("Employee Not Found");
        }

        //--Close Connection from authenticated User.
        connectionHolder.removeConnection();

        return userInfo;
    }

    public boolean checkIfEmployeeAccountIsExisting(DBConnectionHolder connectionHolder, String employeeId) {
        boolean result = false;

        try {
            CallableStatement statement = connectionHolder.getConnection()
                .prepareCall("{? = call SP_CHECK_EXIST_LOGIN(?)}");
            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, String.valueOf(employeeId));

            statement.execute();
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'checkIfEmployeeAccountIsExisting' of AccountRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectionHolder, ReqDtoAddingAccount account) {
        try {
            CallableStatement statement = connectionHolder.getConnection()
                .prepareCall("{? = call SP_CREATE_LOGIN(?, ?, ?, ?)}");

            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, account.getUsername());
            statement.setString(3, account.getPassword());
            statement.setString(4, account.getEmployeeId().toString());
            statement.setString(5, account.getRole());

            statement.execute();
            int result = statement.getInt(1);

            //--Close all connection.
            statement.close();

            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of AccountRepository: " + e);
            return 0;
        }
    }
}
