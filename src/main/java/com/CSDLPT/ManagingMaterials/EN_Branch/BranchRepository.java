package com.CSDLPT.ManagingMaterials.EN_Branch;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BranchRepository {
    private final Logger logger;

    public List<String> findAllBranchIds(DBConnectionHolder conHolder) {
        List<String> result = new ArrayList<>();
        try {
            PreparedStatement statement = conHolder.getConnection().prepareStatement("SELECT MACN FROM LINK2.QLVT_DATHANG.DBO.ChiNhanh");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())   result.add(resultSet.getString("MACN"));

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            logger.info("Error In 'countAllBranch' of BranchRepository: " + e);
        }
        return result;
    }
}
