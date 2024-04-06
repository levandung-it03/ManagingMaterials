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
    <div id="center-page_adding-form">
        <form action="/service/v1/branch/add-employee" method="post" modelAttribute="employee">
            <div class="form-input" id="employeeId">
                <fieldset>
                    <legend>Mã nhân viên</legend>
                    <input name="employeeId" type="number" value="${employee.employeeId}" readonly/>
                </fieldset>
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
    <div id="center-page_list">
        <div id="table-tools">
            <div id="table-description">
                <b>Danh sách</b>
                <span id="quantity">${employeeList.size()} người</span>
            </div>
            <div id="table-search-box">
                <select id="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="0">Thông tin cơ bản</option>
                    <option value="1">Mã</option>
                    <option value="3">Ngày sinh</option>
                    <option value="4">Giới tính</option>
                    <option value="5">Điện thoại</option>
                    <option value="6">Trạng thái</option>
                </select>
                <input type="text" id="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <form action="/service/v1/branch/delete-employee" method="POST">
            <table>
                <thead>
                <tr>
                    <th id="employee-id">
                        Mã
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="base-profile">
                        Thông tin cơ bản
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="birthday">
                        Ngày sinh
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="address">
                        Địa chỉ
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="salary">
                        Lương
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="update">Cập nhật</th>
                    <th id="delete">Xoá</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${employeeList}" var="eachEmployeeInfo">
                    <tr id="${eachEmployeeInfo.employeeId}">
                        <td plain-value="${eachEmployeeInfo.employeeId}" class="employee-id">
                                ${eachEmployeeInfo.employeeId}
                        </td>
                        <td plain-value="${eachEmployeeInfo.identifier} ${eachEmployeeInfo.lastName} ${eachEmployeeInfo.firstName}"
                            class="base-profile">
                            <span class="mock-avatar">${eachEmployeeInfo.firstName.charAt(0)}</span>
                            <div class="employee-info">
                                <b class="employee-name">${eachEmployeeInfo.lastName} ${eachEmployeeInfo.firstName}</b>
                                <p class="identifier">${eachEmployeeInfo.identifier}</p>
                            </div>
                        </td>
                        <td plain-value="${eachEmployeeInfo.birthday}" class="birthday">
                                ${eachEmployeeInfo.birthday}
                        </td>
                        <td plain-value="${eachEmployeeInfo.salary}" class="address">
                                ${eachEmployeeInfo.address}
                        </td>
                        <td plain-value="${eachEmployeeInfo.salary}" class="salary">
                                ${eachEmployeeInfo.salary}
                        </td>
                        <td class="table-row-btn update">
                            <a href="/branch/employee/update-employee?employeeId=${eachEmployeeInfo.employeeId}">
                                <i class="fa-regular fa-pen-to-square"></i>
                            </a>
                        </td>
                        <td class="table-row-btn delete">
                            <button name="deleteBtn" value="${eachEmployeeInfo.employeeId}">
                                <i class="fa-regular fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>
        <div id="table-footer">
            <c:set var="prefixUrl" value="/branch/employee/manage-employee?page=" scope="page"/>
            <div id="table-footer_main">
                    <span class="interact-page-btn">
                        <a href="${prefixUrl}${(currentPage == 1) ? currentPage : (currentPage - 1)}">
                            <i class="fa-solid fa-angle-left"></i>
                        </a>
                    </span>
                <div id="pages-content">
                    <c:if test="${currentPage > 1}">
                            <span class="index-btn">
                                <a href="${prefixUrl}${currentPage - 1}">${currentPage - 1}</a>
                            </span>
                    </c:if>
                    <span class="index-btn">
                            <a href="${prefixUrl}${currentPage}">${currentPage}</a>
                        </span>
                    <c:if test="${employeeList.size() != 0}">
                            <span class="index-btn">
                                <a href="${prefixUrl}${currentPage + 1}">${currentPage + 1}</a>
                            </span>
                    </c:if>
                </div>
                <span class="interact-page-btn">
                        <a href="${prefixUrl}${(employeeList.size() == 0) ? currentPage : (currentPage + 1)}">
                            <i class="fa-solid fa-angle-right"></i>
                        </a>
                    </span>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-employee.js"></script>
</body>
</html>


