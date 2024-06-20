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
    <c:if test="${userInfo.role.getJavaRole() == 'company'}">
        <style>
            .center-page_list table tr th#supplyName,
            .center-page_list table tr td.supplyName {
                width: calc(30% + 2*8%);
            }
        </style>
    </c:if>
</head>
<body>
<span class="hiddenRole" style="display:none">${userInfo.role.getJavaRole()}</span>
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
            <form action="/service/v1/${userInfo.role.getJavaRole()}/add-supply" method="post" modelAttribute="supply">
                <div class="form-input" id="supplyId">
                    <fieldset>
                        <legend>Mã vật tư</legend>
                        <input name="supplyId" type="text" value="${supply.supplyId}" maxlength="4" required/>
                    </fieldset>
                    <div class="form_text-input_err-message"></div>
                </div>
                <div class="form-input" id="supplyName">
                    <fieldset>
                        <legend>Tên vật tư</legend>
                        <input name="supplyName" type="text" value="${supply.supplyName}" maxlength="30" required/>
                    </fieldset>
                    <div class="form_text-input_err-message"></div>
                </div>
                <div class="form-input strong-text" id="unit">
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
                <input type="submit" value="Thêm vật tư">
            </form>
        </div>
    </c:if>
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b>Toàn bộ vật tư </b>
                <span class="quantity"></span>
            </div>
            <div class="right-grid">
                <div class="table-search-box">
                    <select class="search">
                        <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                        <option value="supplyId">Mã vật tư</option>
                        <option value="supplyName">Tên vật tư</option>
                        <option value="unit">Đơn vị tính</option>
                        <option value="quantityInStock">Số lượng tồn</option>
                    </select>
                    <input type="text" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>
        </div>
        <form action="/service/v1/${userInfo.role.getJavaRole()}/delete-supply" method="POST">
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
                    <c:if test="${userInfo.role.getJavaRole() != 'company'}">
                        <th id="update">Cập nhật</th>
                        <th id="delete">Xoá</th>
                    </c:if>
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
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-supply.js"></script>
</body>
</html>