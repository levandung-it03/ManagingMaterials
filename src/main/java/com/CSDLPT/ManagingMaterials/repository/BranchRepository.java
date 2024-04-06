package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class BranchRepository {
    private final Logger logger;

    public int countAll(DBConnectionHolder conHolder) {
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT SoLuongChiNhanh = COUNT(MACN) FROM LINK2.QLVT_DATHANG.DBO.ChiNhanh");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getInt("SoLuongChiNhanh");
        } catch (SQLException e) {
            logger.info("Error In 'countAllBranch' of BranchRepository: " + e);
        }
        return 0;
    }
}
