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
        <form method="POST" action="/service/v1/branch/add-employee" modelAttribute="employee">
            <div class="form-input" id="employeeId">
                <label for="employeeId">Mã nhân viên</label>
                <input name="employeeId" type="number" value="" maxlength="20" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="identifier">
                <label for="identifier">CMND</label>
                <input name="identifier" type="text" value="" maxlength="20" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="lastName">
                <label for="lastName">Họ nhân viên</label>
                <input name="lastName" type="text" value="" maxlength="40" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="firstName">
                <label for="firstName">Tên nhân viên</label>
                <input name="firstName" type="text" value="" maxlength="10" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="address">
                <label for="address">Địa chỉ</label>
                <input name="address" type="text" value="" maxlength="100" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="birthday">
                <label for="birthday">Ngày sinh</label>
                <input name="birthday" type="date" value="" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="salary">
                <label for="salary">Lương</label>
                <input name="salary" type="number" value="" required/>
                <div class="form_text-input_err-message"></div>
            </div>
            <c:if test="${employeeUpdated!= null}">
                <div class="form-input" id="branch">
                    <label for="branch">Chi nhánh</label>
                    <select data="${branch.branchId}" name="branchId">
                        <select name="branchId">
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
    </div>
    <div id="employee-list">

    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-employee.js"></script>
</body>
</html>


