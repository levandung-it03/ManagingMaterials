package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Employee;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private final Logger logger;

    public Optional<Employee> findById(DBConnectionHolder connectHolder, int id) {
        Optional<Employee> result = Optional.empty();
        try {
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("SELECT * FROM Employee WHERE MANV = ?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            result = Optional.of(this.getEmployeeFromResultSet(resultSet));

            //--Close all connection.
            resultSet.close();
            statement.close();
            connectHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'findById' of EmployeeRepository: " + e);
        }
        return result;
    }

    public List<Employee> findAll(DBConnectionHolder connectHolder) {
        List<Employee> result = new ArrayList<>();
        try {
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("SELECT * FROM Employee");
            ResultSet resultSet = statement.executeQuery();
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
            //--May throw SQLException.
            PreparedStatement statement = this.mappingValueIntoCommonPreparedStatement(connectHolder.getConnection(),
                "INSERT INTO Employee VALUES VALUES (%s)", employee);

            //--May throw SQLException.
            int result = statement.executeUpdate();
            
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'findById' of EmployeeRepository: " + e);
            return 0;
        }
    }

    public Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
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
        return null;
    }

    public PreparedStatement mappingValueIntoCommonPreparedStatement(
        Connection connection,
        String unformattedQuery,
        Employee employee
    ) throws SQLException {
        String orderedFields = "MAVN = ?,CMND = ?,HO = ?,TEN = ?,DIACHI = ?,NGAYSINH = ?,LUONG = ?,MACN = ?,TrangThaiXoa = ?";
        PreparedStatement statement = connection.prepareStatement(String.format(unformattedQuery, orderedFields));
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getIdentifier());
        statement.setString(3, employee.getLastName());
        statement.setString(4, employee.getFirstName());
        statement.setString(5, employee.getAddress());
        statement.setDate(6, (Date) employee.getBirthday());
        statement.setInt(7, employee.getSalary());
        statement.setString(8, employee.getBranch());
        statement.setInt(9, 0);
        return statement;
    }
}
