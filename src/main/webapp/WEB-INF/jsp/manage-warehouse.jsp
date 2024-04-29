<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Warehouse</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-warehouse.css">
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
<div id="center-page">
    <div id="center-page_adding-form">
        <form action="/service/v1/branch/add-warehouse" method="post" modelAttribute="warehouse">
            <div class="form-input" id="warehouseId">
                <fieldset>
                    <legend>Mã kho</legend>
                    <input name="warehouseId" type="text" value="${warehouse.warehouseId}" maxlength="4" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="warehouseName">
                <fieldset>
                    <legend>Tên kho</legend>
                    <input name="warehouseName" type="text" value="${warehouse.warehouseName}" maxlength="30" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="address">
                <fieldset>
                    <legend>Địa chỉ</legend>
                    <input name="address" type="text" value="${warehouse.address}" maxlength="100" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <input name="pageNumber" value="${pageNumber}" hidden/>
            <input type="submit" value="Thêm nhân viên">
        </form>
    </div>
    <div id="center-page_list">
        <div id="table-tools">
            <div id="table-description">
                <b>Số lượng </b>
                <span id="quantity">${warehousesList.size()} kho</span>
            </div>
            <div id="table-search-box">
                <select id="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="MANV">Mã kho</option>
                    <option value="TENKHO">Tên kho</option>
                    <option value="DIACHI">Địa chỉ</option>
                    <option value="MACN">Chi nhánh</option>
                </select>
                <input type="text" id="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <form action="/service/v1/branch/delete-warehouse" method="POST">
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
                    <th id="update">Cập nhật</th>
                    <th id="delete">Xoá</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${warehousesList}" var="eachWarehouseInfo">
                    <tr id="${eachWarehouseInfo.warehouseId}">
                        <td plain-value="${eachWarehouseInfo.warehouseId}" class="warehouseId">${eachWarehouseInfo.warehouseId}</td>
                        <td plain-value="${eachWarehouseInfo.warehouseName}" class="warehouseName">${eachWarehouseInfo.warehouseName}</td>
                        <td plain-value="${eachWarehouseInfo.address}" class="address">${eachWarehouseInfo.address}</td>
                        <td style="display:none" plain-value="${eachWarehouseInfo.branch}" class="branch">${eachWarehouseInfo.branch}</td>
                        <td class="table-row-btn update">
                            <a id="${eachWarehouseInfo.warehouseId}">
                                <i class="fa-regular fa-pen-to-square"></i>
                            </a>
                        </td>
                        <td class="table-row-btn delete">
                            <button name="deleteBtn" value="${eachWarehouseInfo.warehouseId}">
                                <i class="fa-regular fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>
        <div id="table-footer">
            <c:set var="prefixUrl" value="/branch/warehouse/manage-warehouse?page=" scope="page"/>
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
                    <c:if test="${warehouseList.size() != 0}">
                            <span class="index-btn">
                                <a href="${prefixUrl}${currentPage + 1}">${currentPage + 1}</a>
                            </span>
                    </c:if>
                </div>
                <span class="interact-page-btn">
                        <a href="${prefixUrl}${(warehouseList.size() == 0) ? currentPage : (currentPage + 1)}">
                            <i class="fa-solid fa-angle-right"></i>
                        </a>
                    </span>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-warehouse.js"></script>

</body>
</html>