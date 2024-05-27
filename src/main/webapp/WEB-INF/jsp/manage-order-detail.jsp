<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Order Detail</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-order-detail.css">
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
        <form action="/service/v1/branch/add-order-detail" method="post" modelAttribute="orderDetail">
            <div class="form-input" id="orderId">
                <fieldset>
                    <legend>Mã đơn đặt hàng</legend>
                    <input name="orderId" type="text" value="${orderDetail.orderId}" readonly/>
                </fieldset>
            </div>
            <div class="form-input" id="supplyId">
                <fieldset>
                    <legend>Mã vật tư</legend>
                    <input name="supplyId" type="text" value="${orderDetail.supplyId}" maxlength="4" required/>
                    <i class="fa-regular fa-pen-to-square"></i>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input strong-text" id="quantitySupply">
                <fieldset>
                    <legend>Số lượng</legend>
                    <input name="quantitySupply" type="number" value="${orderDetail.quantitySupply}" min="0" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="price">
                <fieldset>
                    <legend>Đơn giá</legend>
                    <input name="price" type="number" value="${String.format('%.0f', orderDetail.price)}" min="0"
                           required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div id="rest-components-for-updating"></div>
            <input type="submit" value="Thêm chi tiết DDH">
        </form>
    </div>
    <div id="center-page_list">
        <div id="table-tools">
            <div id="table-description">
                <b><a style="color:blue" href="/branch/order/manage-order">Danh sách đơn</a> > Chi tiết đơn </b>
                <span id="quantity"></span>
            </div>
            <div id="table-search-box">
                <select id="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="supplyId">Mã vật tư</option>
                    <option value="quantitySupply">Số lượng</option>
                    <option value="price">Đơn giá</option>
                </select>
                <input type="text" id="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <form action="/service/v1/branch/delete-order-detail" method="POST">
            <table>
                <thead>
                <tr>
                    <th id="orderId">
                        Mã DDH
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="supplyId">
                        Mã vật tư
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="quantitySupply">
                        Số lượng
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="price">
                        Đơn giá
                        <i class="fa-solid fa-arrow-down-a-z"></i>
                    </th>
                    <th id="update">Cập nhật</th>
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
<div id="select-dialog" class="closed">
    <div id="select-dialog-container">
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
        <div id="closing-dialog-btn"><i class="fa-solid fa-xmark"></i></div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-order-detail.js"></script>
</body>
</html>