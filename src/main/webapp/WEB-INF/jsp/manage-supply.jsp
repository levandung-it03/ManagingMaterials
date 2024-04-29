<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Supply</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-supply.css">
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
        <form action="/service/v1/branch/add-supply" method="post" modelAttribute="supply">
            <div class="form-input" id="supplyId">
                <fieldset>
                    <legend>Mã vật tư</legend>
                    <input name="supplyId" type="text" value="${supply.supplyId}"/>
                </fieldset>
            </div>
            <div class="form-input" id="supplyName">
                <fieldset>
                    <legend>Tên vật tư</legend>
                    <input name="supplyName" type="text" value="${supply.supplyName}" maxlength="30" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="unit">
                <fieldset>
                    <legend>Đơn vị tính</legend>
                    <input name="unit" type="text" value="${supply.unit}" maxlength="15" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div class="form-input" id="quantityInStock">
                <fieldset>
                    <legend>Số lượng tồn</legend>
                    <input name="quantityInStock" type="number" value="${supply.quantityInStock}" min="0" required/>
                </fieldset>
                <div class="form_text-input_err-message"></div>
            </div>
            <div id="rest-components-for-updating"></div>
            <input name="pageNumber" value="${pageNumber}" hidden/>
            <input type="submit" value="Thêm vật tư">
        </form>
    </div>
    <div id="center-page_list">
        <div id="table-tools">
            <div id="table-description">
                <b>Số lượng vật tư</b>
                <span id="quantity">${supplyList.size()}</span>
            </div>
            <div id="table-search-box">
                <select id="search">
                    <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                    <option value="MAVT">Mã vật tư</option>
                    <option value="TENVT">Tên vật tư</option>
                    <option value="DVT">Đơn vị tính</option>
                    <option value="SOLUONGTON">Số lượng tồn</option>
                </select>
                <input type="text" id="search">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <form action="/service/v1/branch/delete-supply" method="POST">
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
                    <th id="update">Cập nhật</th>
                    <th id="delete">Xoá</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${supplyList}" var="eachSupplyInfo">
                    <tr id="${eachSupplyInfo.supplyId}">
                        <td plain-value="${eachSupplyInfo.supplyId}" class="supplyId">${eachSupplyInfo.supplyId}</td>
                        <td plain-value="${eachSupplyInfo.supplyName}" class="supplyName">${eachSupplyInfo.supplyName}</td>
                        <td plain-value="${eachSupplyInfo.unit}" class="unit">${eachSupplyInfo.unit}</td>
                        <td plain-value="${eachSupplyInfo.quantityInStock}" class="quantityInStock">${eachSupplyInfo.quantityInStock}</td>
                        <td class="table-row-btn update">
                            <a id="${eachSupplyInfo.supplyId}">
                                <i class="fa-regular fa-pen-to-square"></i>
                            </a>
                        </td>
                        <td class="table-row-btn delete">
                            <button name="deleteBtn" value="${eachSupplyInfo.supplyId}">
                                <i class="fa-regular fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>
        <div id="table-footer">
            <c:set var="prefixUrl" value="/branch/supply/manage-supply?page=" scope="page"/>
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
                    <c:if test="${supplyList.size() != 0}">
                            <span class="index-btn">
                                <a href="${prefixUrl}${currentPage + 1}">${currentPage + 1}</a>
                            </span>
                    </c:if>
                </div>
                <span class="interact-page-btn">
                        <a href="${prefixUrl}${(supplyList.size() == 0) ? currentPage : (currentPage + 1)}">
                            <i class="fa-solid fa-angle-right"></i>
                        </a>
                    </span>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<%--<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-supply.js"></script>--%>
</body>
</html>