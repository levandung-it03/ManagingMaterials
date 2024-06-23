<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Supplies Exportation Detail</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-supplies-exportation-detail.css">
    <c:if test="${userInfo.role.getJavaRole() == 'company'}">
        <style>
            .center-page_list table tr th#price,
            .center-page_list table tr td.price {
                width: calc(30% + 10%);
            }
        </style>
    </c:if>
</head>
<body>
<span class="hiddenRole" style="display:none">${userInfo.role.getJavaRole()}</span>
<span class="hiddenEmployeeId" style="display:none">${userInfo.employeeId}</span>
<%--    <%@ include file="/WEB-INF/jsp/category.jsp" %>--%>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<div id="message-block">
    <c:if test="${errorMessage != null}">
        <div class="error-service-message">
            <span>${errorMessage}</span>
            <i id="error-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
        </div>
    </c:if>
    <c:if test="${succeedMessage != null}">
        <div class="succeed-service-message">
            <span>${succeedMessage}</span>
            <i id="succeed-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
        </div>
    </c:if>
</div>
<div class="center-page">
    <c:if test="${userInfo.role.getJavaRole() != 'company'}">
        <div class="center-page_adding-form">
            <form action="/service/v1/${userInfo.role.getJavaRole()}/add-supplies-exportation-detail" method="post" modelAttribute="exportationDetail">
                <div class="form-input" id="suppliesExportationId">
                    <fieldset>
                        <legend>Mã phiếu xuất</legend>
                        <input name="suppliesExportationId" type="text" value="${exportationDetail.suppliesExportationId}"
                               readonly/>
                    </fieldset>
                </div>
                <div class="form-input" id="supplyId">
                    <fieldset>
                        <legend>Mã vật tư</legend>
                        <input name="supplyId" type="text" value="${exportationDetail.supplyId}" maxlength="4" readonly required/>
                        <i class="fa-regular fa-pen-to-square"></i>
                    </fieldset>
                    <div class="form_text-input_err-message"></div>
                </div>
                <div class="form-input" id="suppliesQuantity">
                    <fieldset>
                        <legend>Số lượng</legend>
                        <input name="suppliesQuantity" type="number" value="${exportationDetail.suppliesQuantity}" min="0" required/>
                        <input name="quantityInStock" type="number" value="0" min="0" readonly hidden/>
                    </fieldset>
                    <div class="form_text-input_err-message"></div>
                </div>
                <div class="form-input" id="price">
                    <fieldset>
                        <legend>Đơn giá</legend>
                        <input name="price" type="number" value="${String.format('%.0f', exportationDetail.price)}" min="0"
                               required/>
                    </fieldset>
                    <div class="form_text-input_err-message"></div>
                </div>
                <div id="rest-components-for-updating"></div>
                <input type="submit" value="Thêm chi tiết phiếu">
            </form>
        </div>
    </c:if>
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b><a style="color:blue" href="/${userInfo.role.getJavaRole()}/supplies-exportation/manage-supplies-exportation">Toàn bộ phiếu xuất</a> > Chi tiết phiếu </b>
                <span class="quantity"></span>
            </div>
            <div class="table-search-box">
                <select class="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="supplyId">Mã vật tư</option>
                    <option value="suppliesQuantity">Số lượng</option>
                    <option value="price">Đơn giá</option>
                </select>
                <input type="text" class="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <table>
            <thead>
            <tr>
                <th id="suppliesExportationId">
                    Mã phiếu xuất
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <th id="supplyId">
                    Mã vật tư
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <th id="suppliesQuantity">
                    Số lượng
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <th id="price">
                    Đơn giá
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <c:if test="${userInfo.role.getJavaRole() != 'company'}">
                    <th id="update">Cập nhật</th>
                </c:if>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
        <div class="table-footer">
            <div class="table-footer_main"></div>
        </div>
    </div>
</div>
<c:if test="${userInfo.role.getJavaRole() != 'company'}">
    <div class="select-dialog closed">
        <div class="select-dialog-container">
            <span class="form-title">Vật tư</span>
            <table>
                <thead>
                <tr>
                    <th id="supplyId">
                        Mã
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="supplyName">
                        Tên vật tư
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="unit">
                        Đơn vị tính
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="quantityInStock">
                        Số lượng tồn
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div class="table-footer">
                <div class="table-footer_main"></div>
            </div>
            <div class="closing-dialog-btn"><i class="fa-solid fa-xmark"></i></div>
        </div>
    </div>
</c:if>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/Dialogs.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-supplies-exportation-detail.js"></script>
</body>
</html>