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
    <c:if test="${userInfo.role.getJavaRole() == 'company'}">
        <style>
            .center-page_list table tr th#warehouseName,
            .center-page_list table tr td.warehouseName {
                width: calc(30% + 2*7.5%);
            }
        </style>
    </c:if>
</head>
<body>
<span class="hiddenRole" style="display:none">${userInfo.role.getJavaRole()}</span>
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
            <form action="/service/v1/${userInfo.role.getJavaRole()}/add-warehouse" method="post" modelAttribute="warehouse">
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
                <input type="submit" value="Thêm kho">
            </form>
        </div>
    </c:if>
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b>Toàn bộ kho </b>
                <span class="quantity"></span>
            </div>
            <div class="right-grid">
                <div class="select-branch-to-search">
                    <fieldset>
                        <legend>Chi nhánh</legend>
                        <select name="searchingBranch" ${userInfo.role.getJavaRole() == 'company' ? '' : 'disabled'} data="${userInfo.branch}">
                            <c:forEach items="${branchesList}" var="branch">
                                <option value="${branch.trim()}">${branch.trim()}</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </div>
                <div class="table-search-box">
                    <select class="search">
                        <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                        <option value="warehouseId">Mã kho</option>
                        <option value="warehouseName">Tên kho</option>
                        <option value="address">Địa chỉ</option>
                    </select>
                    <input type="text" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>
        </div>
        <form action="/service/v1/${userInfo.role.getJavaRole()}/delete-warehouse" method="POST">
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
                    <c:if test="${userInfo.role.getJavaRole() != 'company'}">
                        <th id="update">Cập nhật</th>
                        <th id="delete">Xoá</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </form>
        <div class="table-footer">
            <div class="table-footer_main"></div>
        </div>
    </div>
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-warehouse.js"></script>

</body>
</html>