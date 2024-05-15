<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Order</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-order.css">
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
        <form action="/service/v1/branch/add-order" method="post" modelAttribute="order">
            <div class="form-input" id="orderId">
                <fieldset>
                    <legend>Mã đơn đặt hàng</legend>
                    <input name="orderId" type="text" value="${order.orderId}" maxlength="8" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="createdDate">
                <fieldset>
                    <legend>Ngày tạo</legend>
                    <input name="createdDate" type="date"
                           value="<fmt:formatDate pattern="yyyy-MM-dd" value="${order.createdDate}" />" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="supplier">
                <fieldset>
                    <legend>Nhà cung cấp</legend>
                    <input name="supplier" type="text" value="${order.supplier}" maxlength="100" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="employeeId">
                <fieldset>
                    <legend>Mã nhân viên</legend>
                    <input name="employeeId" type="number" value="${order.employeeId}" readonly/>
                </fieldset>
            </div>
            <div class="form-input" id="warehouseId">
                <fieldset>
                    <legend>Mã kho</legend>
                    <input name="warehouseId" type="text" value="${order.warehouseId}" maxlength="4"
                           required/>
                    <i class="fa-regular fa-pen-to-square"></i>
                </fieldset>
            </div>
            <div id="rest-components-for-updating"></div>
            <input type="submit" value="Thêm đơn đặt hàng">
        </form>
    </div>
    <div id="center-page_list">
        <div id="table-tools">
            <div id="table-description">
                <b>Danh sách đơn đặt hàng </b>
                <span id="quantity"></span>
            </div>
            <div id="table-search-box">
                <select id="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="MasoDDH">Mã đơn đặt hàng</option>
                    <option value="NhaCC">Nhà cung cấp</option>
                    <option value="NGAY">Ngày tạo</option>
                    <option value="MANV">Mã nhân viên</option>
                    <option value="MAKHO">Mã kho</option>
                </select>
                <input type="text" id="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <form action="/service/v1/branch/delete-order" method="POST">
            <table>
                <thead>
                <tr>
                    <th id="orderId">
                        Mã
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="supplier">
                        Nhà cung cấp
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="createdDate">
                        Ngày tạo
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="employeeId">
                        Mã nhân viên
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="warehouseId">
                        Mã kho
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="detail">Chi tiết</th>
                    <th id="update">Cập nhật</th>
                    <th id="delete">Xoá</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </form>
        <div id="table-footer">
            <div id="table-footer_main"></div>
        </div>
    </div>
</div>
<div id="select-dialog" class="closed">
    <div id="select-dialog-container">
        <span id="form-title">Kho</span>
        <table>
            <thead>
            <tr>
                <th id="warehouseId">
                    Mã
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <th id="warehouseName">
                    Tên kho
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
                <th id="address">
                    Địa chỉ
                    <i class="fa-solid fa-arrow-down-a-z"></i>
                </th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
        <div id="table-footer">
            <div id="table-footer_main"></div>
        </div>
        <div id="closing-dialog-btn"><i class="fa-solid fa-xmark"></i></div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-order.js"></script>
</body>
</html>