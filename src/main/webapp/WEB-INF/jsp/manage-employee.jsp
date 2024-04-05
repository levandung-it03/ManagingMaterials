<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Employee</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-employee.css">
</head>
<body>
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
<div id="center-page">
    <div id="add-employee">
        <form action="/service/v1/branch/add-employee" method="post" modelAttribute="employee">
            <div class="form-input" id="employeeId">
                <fieldset>
                    <legend>Mã nhân viên</legend>
                    <input name="employeeId" type="number" value="${employee.employeeId}" maxlength="20" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="identifier">
                <fieldset>
                    <legend>CMND</legend>
                    <input name="identifier" type="text" value="${employee.identifier}" maxlength="20" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="lastName">
                <fieldset>
                    <legend>Họ nhân viên</legend>
                    <input name="lastName" type="text" value="${employee.lastName}" maxlength="40" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="firstName">
                <fieldset>
                    <legend>Tên nhân viên</legend>
                    <input name="firstName" type="text" value="${employee.firstName}" maxlength="10" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="address">
                <fieldset>
                    <legend>Địa chỉ</legend>
                    <input name="address" type="text" value="${employee.address}" maxlength="100" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="birthday">
                <fieldset>
                    <legend>Ngày sinh</legend>
                    <input name="birthday" type="date" value="${employee.birthday}" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="salary">
                <fieldset>
                    <legend>Lương</legend>
                    <input name="salary" type="number" value="${employee.salary}" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <c:if test="${employee.branch != null}">
                <div class="form-input" id="branch">
                    <label for="branch">Chi nhánh</label>
                    <select data="${branch.branchId}" name="branchId">
                        <option value="" disabled hidden selected>Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${branchList}">
                            <option value="${branch.branchId}">${branch.branchId}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
            <input name="pageNumber" value="${pageNumber}" hidden/>
            <input type="submit" value="Thêm nhân viên">
        </form>
        <span type="number" class="hidden-data-fields" name="branchesQuantity">${branchesQuantity}</span>
        <span type="number" class="hidden-data-fields" name="lastEmployeeId">${lastEmployeeId}</span>
    </div>
    <div id="employee-list">

    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-employee.js"></script>
</body>
</html>


