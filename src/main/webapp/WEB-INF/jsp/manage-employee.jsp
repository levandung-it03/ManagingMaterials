<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
<div class="center-page">
    <div class="center-page_adding-form">
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
                    <input name="salary" type="number" value="${String.format("%.0f", employee.salary)}" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div id="rest-components-for-updating"></div>
            <input type="submit" value="Thêm nhân viên" />
        </form>
        <div id="branchesList" style="display:none">
            <c:forEach items="${branchesList}" var="branch">
                <span class="hidden-data-fields" type="blacks text" name="branch">${branch}</span>
            </c:forEach>
        </div>
    </div>
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b>Số lượng </b>
                <span class="quantity"></span>
            </div>
            <div class="right-grid">
                <div class="select-branch-to-search">
                    <fieldset>
                        <legend>Chi nhánh</legend>
                        <select name="searchingBranch" disabled="${userInfo.role == 'CONGTY' ? 'fasle' : 'true'}" data="${userInfo.branch}">
                            <c:forEach items="${branchesList}" var="branch">
                                <option value="${branch.trim()}">${branch.trim()}</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </div>
                <div class="table-search-box">
                    <select class="search">
                        <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                        <option value="employeeId">Mã nhân viên</option>
                        <option value="identifier">CMND</option>
                        <option value="lastName">Họ</option>
                        <option value="firstName">Tên</option>
                        <option value="fullName">Họ và tên</option>
                        <option value="birthday">Ngày sinh</option>
                        <option value="address">Địa chỉ</option>
                        <option value="salary">Lương</option>
                    </select>
                    <input type="text" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>
        </div>
        <form action="/service/v1/branch/delete-employee" method="POST">
            <table>
                <thead>
                <tr>
                    <th id="employeeId">
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
                    <th id="addAccount">Thêm tài khoản</th>
                    <th id="delete">Xoá</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </form>
        <div class="table-footer">
            <div class="table-footer_main"></div>
        </div>
    </div>
</div>
<div id="form-dialog" class="closed">
    <div id="form-dialog_surrounding-frame"></div>
    <div id="form-dialog_adding-account">
        <form action="/service/v1/branch/add-account" method="post" modelAttribute="account">
            <span class="form-title">Tạo login</span>
            <div class="form-input" id="employeeId">
                <fieldset>
                    <legend>Mã nhân viên</legend>
                    <input name="employeeId" type="number" value="" readonly/>
                </fieldset>
            </div>
            <div class="form-input strong-text" id="fullName">
                <fieldset>
                    <legend>Họ và tên</legend>
                    <input name="fullName" type="text" value="" maxlength="40" readonly/>
                </fieldset>
            </div>
            <div class="form-select" id="role">
                <fieldset>
                    <legend>Vai trò</legend>
                    <select name="role">
                        <option value="CHINHANH">Chi nhánh</option>
                        <option value="USER">Người dùng</option>
                    </select>
                </fieldset>
            </div>
            <div class="form-input" id="username">
                <fieldset>
                    <legend>Tên đăng nhập</legend>
                    <input name="username" type="text" value="" maxlength="10" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="password">
                <fieldset>
                    <legend>Mật khẩu</legend>
                    <input name="password" type="password" value="" maxlength="20" required/>
                    <div class="password_toggle-hidden">
                        <i id="password" class="show-pass fa-solid fa-eye"></i>
                        <i id="password" class="hide-pass hidden fa-regular fa-eye-slash"></i>
                    </div>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="retypePassword">
                <fieldset>
                    <legend>Mật khẩu xác nhận</legend>
                    <input name="retypePassword" type="password" value="" maxlength="20" required/>
                    <div class="password_toggle-hidden">
                        <i id="password" class="show-pass fa-solid fa-eye"></i>
                        <i id="password" class="hide-pass hidden fa-regular fa-eye-slash"></i>
                    </div>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <button name="submit">Thêm tài khoản</button>
        </form>
        <div class="closing-dialog-btn"><i class="fa-solid fa-xmark"></i></div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-employee.js"></script>
</body>
</html>