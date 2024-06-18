<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report For Order Dont Have Import</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report-for-order-dont-have-import.css">
</head>
<body>
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
<div class="center-page report-pages">
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b>Danh sách đơn đặt hàng chưa có phiếu nhập </b>
                <span class="quantity"></span>
            </div>
            <div class="right-grid">
                <div class="table-search-box">
                    <select class="search">
                        <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                        <option value="orderId">Mã</option>
                        <option value="createdDate">Ngày tạo</option>
                        <option value="supplier">Nhà cung cấp</option>
                        <option value="employeeFullName">Họ tên nhân viên</option>
                        <option value="supplyName">Tên vật tư</option>
                        <option value="suppliesQuantity">Số lượng đặt</option>
                        <option value="price">Đơn giá</option>
                    </select>
                    <input type="text" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>
        </div>
        <table>
            <thead>
            <tr>
                <th id="orderId">Mã<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="createdDate">Ngày tạo<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="supplier">Nhà cung cấp<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="employeeFullName">Họ tên nhân viên<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="supplyName">Tên vật tư<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="suppliesQuantity">Số lượng đặt<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th id="price">Đơn giá<i class="fa-solid fa-arrow-down-a-z"></i></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div class="table-footer">
            <div class="table-footer_main"></div>
        </div>
    </div>
</div>
<div class="report-supporting-buttons">
    <a class="report-supporting-buttons_preview">
        Xem trước&emsp;<i class="fa-solid fa-file-pdf"></i>
    </a>
</div>
<div class="preview-table-container closed"></div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/PdfFilesExportation.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/report-for-order-dont-have-import.js"></script>
</body>
</html>