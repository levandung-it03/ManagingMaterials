package com.CSDLPT.ManagingMaterials.EN_Employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResDtoReportForEmployeeActivities {
    private Date createdDate;
    private String ticketId;
    private String ticketType;
    private String customerFullName;
    private String supplyName;
    private Integer suppliesQuantity;
    private Double price;
    private Double totalPrice;
}
