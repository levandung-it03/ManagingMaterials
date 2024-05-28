package com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class InnerJoinObject {
    private final String queryFormat = "INNER JOIN (SELECT %s FROM %s %s) AS %s ON %s.%s = %s.%s";
    private String left;
    private String right;
    private String bridge;
    private String fields;
    private String rightEntityConditions;

    public String buildQuery() throws NoSuchFieldException {
        StaticUtilMethods staticUtilMethods = new StaticUtilMethods();
        String subNameOfRightEntity = staticUtilMethods.columnNameStaticDictionary(this.right).get(2);
        
        //--INNER JOIN (SELECT %s FROM %s%s) AS %s ON %s.%s = %s.%s
        return String.format(this.queryFormat,
        //--Inside the parentheses (selecting right entity)
            this.fields, this.right,
            this.rightEntityConditions == null ? "" : " WHERE " + this.rightEntityConditions,
        //--Sub-name of the selecting result.
            subNameOfRightEntity,
        //--The rest parts of joining query.
            left, bridge, subNameOfRightEntity, bridge
        );
    }

    public static String mergeQuery(List<InnerJoinObject> objs) {
        StringBuilder result = new StringBuilder();
        objs.forEach(obj -> {
            try {
                result.append(
                    obj.buildQuery()
                ).append(" ");
            }
            catch (NoSuchFieldException e) { System.out.println(e.getMessage() + ", " + e.getCause()); }
        });
        return result.toString();
    }
}
