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
    <c:if test="${userInfo.role.getJavaRole() == 'company'}">
        <style>
            .center-page_list table tr th#supplier,
            .center-page_list table tr td.supplier {
                width: calc(20% + 2*8%);
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
                <form action="/service/v1/${userInfo.role.getJavaRole()}/add-order" method="post" modelAttribute="order">
                    <div class="form-input" id="orderId">
                        <fieldset>
                            <legend>Mã đơn đặt hàng</legend>
                            <input name="orderId" type="text" value="${order.orderId}" maxlength="8" required/>
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
                    <div class="form-input" id="warehouseIdAsFk">
                        <fieldset>
                            <legend>Mã kho</legend>
                            <input name="warehouseIdAsFk" type="text" value="${order.warehouseIdAsFk}" maxlength="4"
                                   required readonly/>
                            <i class="fa-regular fa-pen-to-square"></i>
                        </fieldset>
                    </div>
                    <div id="rest-components-for-updating"></div>
                    <input type="submit" value="Thêm đơn đặt hàng">
                </form>
            </div>
        </c:if>
        <div class="center-page_list">
            <div class="table-tools">
                <div class="table-description">
                    <b>Toàn bộ đơn đặt hàng </b>
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
                            <option value="orderId">Mã đơn đặt hàng</option>
                            <option value="supplier">Nhà cung cấp</option>
                            <option value="createdDate">Ngày tạo</option>
                            <option value="employeeIdAsFk">Mã nhân viên</option>
                            <option value="warehouseIdAsFk">Mã kho</option>
                        </select>
                        <input type="text" class="search">
                        <i class="fa-solid fa-magnifying-glass"></i>
                    </div>
                </div>
            </div>
            <form action="/service/v1/${userInfo.role.getJavaRole()}/delete-order" method="POST">
                <table>
                    <thead>
                    <tr>
                        <th id="orderId">
                            Mã
                            <i class="fa-solid fa-arrow-down-a-z"></i>
                        </th>
                        <th id="employeeIdAsFk">
                            Mã nhân viên
                            <i class="fa-solid fa-arrow-down-a-z"></i>
                        </th>
                        <th id="supplier">
                            Nhà cung cấp
                            <i class="fa-solid fa-arrow-down-a-z"></i>
                        </th>
                        <th id="warehouseIdAsFk">
                            Mã kho
                            <i class="fa-solid fa-arrow-down-a-z"></i>
                        </th>
                        <th id="createdDate">
                            Ngày tạo
                            <i class="fa-solid fa-arrow-down-a-z"></i>
                        </th>
                        <th id="detail">Chi tiết</th>
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
    <c:if test="${userInfo.role.getJavaRole() != 'company'}">
        <div class="select-dialog closed">
            <div class="select-dialog-container">
                <span class="form-title">Kho</span>
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
                <div class="table-footer">
                    <div class="table-footer_main"></div>
                </div>
                <div class="closing-dialog-btn"><i class="fa-solid fa-xmark"></i></div>
            </div>
        </div>
    </c:if>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/Dialogs.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-order.js"></script>
</body>
</html>