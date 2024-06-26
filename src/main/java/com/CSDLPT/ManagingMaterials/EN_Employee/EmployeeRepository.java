package com.CSDLPT.ManagingMaterials.EN_Employee;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ReqDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ResDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
            String updateEmployeeDeleteStatusQuery = "UPDATE NHANVIEN SET TRANGTHAIXOA = 1 WHERE MANV = ?";
            CallableStatement statement = connectionHolder.getConnection()
                .prepareCall(updateEmployeeDeleteStatusQuery);
//                    .prepareCall("{call SP_DELETE_EMPLOYEE(?)}");
            statement.setInt(1, employeeId);

            //--Retrieve affected rows to know if our Query worked correctly.
            result = statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("Error In 'delete' of EmployeeRepository: " + e);
        }
        return result;
    }


    public List<Employee> findAllEmployeesByStoredProc(
        DBConnectionHolder connectHolder,
        String selectedBranch,
        ResDtoUserInfo userInfo
    ) {
        List<Employee> resultList = new java.util.ArrayList<>(List.of());
        try {
            //--Prepare data to execute Query Statement.
            CallableStatement statement = connectHolder.getConnection()
                .prepareCall("{call SP_REPORT_EMPLOYEES_LIST(?, ?, ?)}");
            statement.setInt(1, userInfo.getEmployeeId());
            statement.setString(2, selectedBranch);
            statement.setString(3, userInfo.getBranch());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                resultList.add(Employee.builder()
                    .identifier(resultSet.getString("CMND"))
                    .employeeId(resultSet.getInt("MANV"))
                    .lastName(resultSet.getString("HO"))
                    .firstName(resultSet.getString("TEN"))
                    .address(resultSet.getString("DIACHI"))
                    .birthday(resultSet.getDate("NGAYSINH"))
                    .salary(resultSet.getDouble("LUONG"))
                    .branch(resultSet.getString("MACN"))
                    .deletedStatus(resultSet.getInt("TrangThaiXoa"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAllEmployeesByStoredProc' of EmployeeRepository: " + e);
        }
        return resultList;
    }

    public List<ResDtoReportForEmployeeActivities> findAllEmployeeActivities(
        DBConnectionHolder connectHolder,
        ReqDtoReportForEmployeeActivities requiredInfoToSearchEmpActivities
    ) {
        List<ResDtoReportForEmployeeActivities> resultList = new java.util.ArrayList<>(List.of());
        try {
            //--Prepare data to execute Query Statement.
            CallableStatement statement = connectHolder.getConnection()
                .prepareCall("{call SP_REPORT_EMPLOYEE_WORKING_STATUS(?, ?, ?)}");

            statement.setInt(1, requiredInfoToSearchEmpActivities.getEmployeeId());
            statement.setDate(2, staticUtilMethods
                .dateUtilToSqlDate(requiredInfoToSearchEmpActivities.getStartingDate()));
            statement.setDate(3, staticUtilMethods
                .dateUtilToSqlDate(requiredInfoToSearchEmpActivities.getEndingDate()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String customer = resultSet.getString("HOTENKH");
                resultList.add(ResDtoReportForEmployeeActivities.builder()
                    .createdDate(resultSet.getDate("NGAY"))
                    .ticketId(resultSet.getString("MAPHIEU"))
                    .ticketType(resultSet.getString("LOAIPHIEU"))
                    .customerFullName(customer.trim().equalsIgnoreCase("NULL") ? "Không" : customer)
                    .supplyName(resultSet.getString("TENVT"))
                    .suppliesQuantity(resultSet.getString("SOLUONG"))
                    .price(resultSet.getDouble("DONGIA"))
                    .totalPrice(resultSet.getDouble("TRIGIA"))
                    .build());
            }

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAllEmployeeActivities' of EmployeeRepository: " + e);
        }
        return resultList;
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
        statement.setTimestamp(6, new Timestamp(employee.getBirthday().getTime()));
        statement.setDouble(7, employee.getSalary());
        statement.setString(8, employee.getBranch());
        statement.setInt(9, 0);
    }
}
