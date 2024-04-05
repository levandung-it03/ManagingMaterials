package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Employee;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Logger logger;

    public boolean isExistingEmployee(DBConnectionHolder conHolder, Employee employee) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{call SP_CHECK_EXIST_CMND(?, ?)}");
            statement.setInt(1, employee.getEmployeeId());
            statement.setString(2, employee.getIdentifier());
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

    public Optional<Employee> findById(DBConnectionHolder connectHolder, int id) {
        //--Using a 'Optional' var to make our logic easily to control.
        Optional<Employee> result = Optional.empty();

        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection()
                .prepareStatement("SELECT * FROM NhanVien WHERE MANV = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            //--May throw NullPointerException.
            if (resultSet.next())
                result = Optional.of(this.getEmployeeFromResultSet(resultSet));

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            logger.info("Error In 'findById' of EmployeeRepository: " + e);
        }
        return result;
    }

    public Integer findTheLastEmployeeId(DBConnectionHolder connectHolder) {
        //--Using a 'Optional' var to make our logic easily to control.
        Integer result = null;

        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection()
                .prepareStatement("SELECT TOP 1 MANV FROM NhanVien ORDER BY MANV DESC");
            ResultSet resultSet = statement.executeQuery();

            //--May throw NullPointerException.
            if (resultSet.next())
                result = resultSet.getInt("MANV");

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            logger.info("Error In 'findTheLastEmployeeId' of EmployeeRepository: " + e);
        }
        return result;
    }

    public List<Employee> findAll(DBConnectionHolder connectHolder) {
        //--Using a 'List<Employee>' var as our result.
        List<Employee> result = new ArrayList<>();

        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("SELECT * FROM Employee");
            ResultSet resultSet = statement.executeQuery();

            //--Mapping all data into 'List<Employee>' result var.
            while (resultSet.next()) {
                result.add(this.getEmployeeFromResultSet(resultSet));
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
            connectHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'findById' of EmployeeRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectHolder, Employee employee) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = this.mapDataIntoCommonStatement(
                connectHolder.getConnection(),
                "INSERT INTO NhanVien (%s) VALUES (%s)", employee
            );

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

    /**SQL Server: This method is used to retrieve each data inside ResultSet.**/
    public Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        return Employee.builder()
            .employeeId(resultSet.getInt("MAVN"))
            .identifier(resultSet.getString("CMND"))
            .lastName(resultSet.getString("HO"))
            .firstName(resultSet.getString("TEN"))
            .address(resultSet.getString("DIACHI"))
            .birthday(resultSet.getDate("NGAYSINH"))
            .salary(resultSet.getInt("LUONG"))
            .branch(resultSet.getString("MACN"))
            .deletedStatus(resultSet.getInt("TrangThaiXoa"))
            .build();
    }

    /**SQL Server: This method is used to map data into the common PreparedStatement, which has all fields of Employee.**/
    public PreparedStatement mapDataIntoCommonStatement(Connection connection, String queryFormat, Employee employee
    ) throws SQLException {
        String orderedFields = "MANV ,CMND ,HO ,TEN ,DIACHI ,NGAYSINH ,LUONG ,MACN ,TrangThaiXoa";
        String allFields = "? ,? ,? ,? ,? ,? ,? ,? ,?";
        PreparedStatement statement = connection.prepareStatement(String.format(queryFormat, orderedFields, allFields));
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getIdentifier());
        statement.setString(3, employee.getLastName());
        statement.setString(4, employee.getFirstName());
        statement.setString(5, employee.getAddress());
        statement.setDate(6, Date.valueOf(simpleDateFormat.format(employee.getBirthday())));
        statement.setInt(7, employee.getSalary());
        statement.setString(8, employee.getBranch());
        statement.setInt(9, 0);
        return statement;
    }
}
