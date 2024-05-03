package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    public boolean isExistingEmployeeByIdentifier(DBConnectionHolder connectHolder, String identifier) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = connectHolder.getConnection().prepareCall("{call SP_CHECK_EXIST_CMND(?)}");
            statement.setString(1, identifier);
            ResultSet resultSet = statement.executeQuery();

            //--If at least one Employee is existing.
            if (resultSet.next()) result = true;

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'existingEmployeeIdentifier' of EmployeeRepository: " + e);
        }
        return result;
    }

    public Integer getNextEmployeeId(DBConnectionHolder connectHolder, String branch) {
        Integer result = null;

        try {
            //--Prepare data to execute Query Statement.
            CallableStatement statement = connectHolder.getConnection().prepareCall("{call SP_GET_EMPLOYEE_ID(?, ?)}");
            statement.setString(1, branch);
            statement.registerOutParameter(2, Types.INTEGER);

            statement.execute();

            //--May throw NullPointerException.
            result = statement.getInt(2);

            //--Close all connection.
            statement.close();
        } catch (SQLException | NullPointerException e) {
            logger.info("Error In 'getNextEmployeeId' of EmployeeRepository: " + e);
        }
        return result;
    }

    public List<Employee> findAll(DBConnectionHolder connectHolder, PageObject pageObj) {
        //--Using a 'List<Employee>' var as our result.
        List<Employee> result = new ArrayList<>();

        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement(
                String.format("{call SP_LIST_ALL_EMPLOYEES(%s, %s)}", pageObj.getPage(), pageObj.getSize())
            );
            ResultSet resultSet = statement.executeQuery();

            //--Mapping all data into 'List<Employee>' result var.
            while (resultSet.next()) {
                result.add(
                    Employee.builder()
                        .employeeId(resultSet.getInt("MANV"))
                        .identifier(resultSet.getString("CMND").trim())
                        .lastName(resultSet.getString("HO").trim())
                        .firstName(resultSet.getString("TEN").trim())
                        .address(resultSet.getString("DIACHI").trim())
                        .birthday(resultSet.getDate("NGAYSINH"))
                        .salary(resultSet.getDouble("LUONG"))
                        .branch(resultSet.getString("MACN").trim())
                        .build()
                );
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAll' of EmployeeRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectHolder, Employee employee) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
                INSERT INTO NhanVien (MANV ,CMND ,HO ,TEN ,DIACHI ,NGAYSINH ,LUONG ,MACN ,TrangThaiXoa)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """);
            this.mapDataIntoStatement(statement, employee);

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of EmployeeRepository: " + e);
            return 0;
        }
    }

    public int update(DBConnectionHolder connectionHolder, Employee employee, String oldBranch) {
        try {
            /** SP_UPDATE_EMPLOYEE
             *  @MANV INT,
             *  @CMND NVARCHAR(10),
             *  @HO NVARCHAR(20),
             *  @TEN NVARCHAR(50),
             *  @DIACHI NVARCHAR(100),
             *  @NGAYSINH DATE,
             *  @LUONG FLOAT,
             *  @MACN_CU NVARCHAR(3),
             *  @MACN_MOI NVARCHAR(3)
             */
            CallableStatement statement = connectionHolder.getConnection()
                .prepareCall("{call SP_UPDATE_EMPLOYEE(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            this.mapDataIntoStatement(statement, employee);
            statement.setString(9, oldBranch);

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'update' of EmployeeRepository: " + e);
            return 0;
        }
    }

    public int delete(DBConnectionHolder connectionHolder, int employeeId) {
        int result = 0;
        try {
            CallableStatement statement = connectionHolder.getConnection().prepareCall("{call SP_DELETE_EMPLOYEE(?)}");
            statement.setInt(1, employeeId);

            //--Retrieve affected rows to know if our Query worked correctly.
            result = statement.executeUpdate();

            connectionHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'delete; of EmployeeRepository: " + e);
        }
        return result;
    }

    /**
     * SQL Server: This method is used to map data into the common PreparedStatement, which has all fields of Employee.
     **/
    public void mapDataIntoStatement(PreparedStatement statement, Employee employee) throws SQLException {
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getIdentifier());
        statement.setString(3, employee.getLastName());
        statement.setString(4, employee.getFirstName());
        statement.setString(5, employee.getAddress());
        statement.setDate(6, staticUtilMethods.dateUtilToSqlDate(employee.getBirthday()));
        statement.setDouble(7, employee.getSalary());
        statement.setString(8, employee.getBranch());
        statement.setInt(9, 0);
    }
}