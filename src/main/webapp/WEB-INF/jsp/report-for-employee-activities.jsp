<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report For Employee</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report-for-employee-activities.css">
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
<div class="center-page report-pages">
    <div class="center-page_list">
        <div class="table-tools">
            <div class="table-description">
                <b>Báo cáo hoạt động nhân viên - Chọn từ danh sách</b>
                <span class="quantity"></span>
            </div>
            <div class="right-grid">
                <div class="select-branch-to-search">
                    <fieldset>
                        <legend>Chi nhánh</legend>
                        <select name="searchingBranch" ${userInfo.role.getJavaRole() == 'company' ? '' : 'disabled'}
                                data="${userInfo.branch}">
                            <c:forEach items="${branchesList}" var="branch">
                                <option value="${branch.trim()}">${branch.trim()}</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </div>
                <div class="table-search-box">
                    <select class="search">
                        <option value="" selected disabled hidden>Chọn trường cần tìm</option>
                        <option value="employeeId">Mã</option>
                        <option value="identifier">CMND</option>
                        <option value="lastName">Họ</option>
                        <option value="firstName">Tên</option>
                        <option value="birthday">Ngày sinh</option>
                        <option value="address">Địa chỉ</option>
                        <option value="salary">Lương</option>
                        <option value="branch">Chi nhánh</option>
                        <option value="deletedStatus">Trạng thái xoá</option>
                    </select>
                    <input type="text" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>
        </div>
        <table>
            <thead>
            <tr>
                <th class="employeeId">Mã<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="identifier">CMND<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="lastName">Họ<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="firstName">Tên<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="birthday">Ngày sinh<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="address">Địa chỉ<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="salary">Lương<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="branch">Chi nhánh<i class="fa-solid fa-arrow-down-a-z"></i></th>
                <th class="deletedStatus">Trạng thái xoá<i class="fa-solid fa-arrow-down-a-z"></i></th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
        <div class="table-footer">
            <div class="table-footer_main"></div>
        </div>
    </div>
    <div class="center-page_more-info">
        <div class="more-info-title">
            <span>Báo cáo hoạt động nhân viên - Bảng các thông tin phụ</span>
        </div>
        <div class="center-page_more-info_table-info-block">
            <table class="selected-instance">
                <thead>
                <tr>
                    <th class="employeeId">Mã</i></th>
                    <th class="identifier">CMND</i></th>
                    <th class="lastName">Họ</i></th>
                    <th class="firstName">Tên</i></th>
                    <th class="birthday">Ngày sinh</i></th>
                    <th class="address">Địa chỉ</i></th>
                    <th class="salary">Lương</i></th>
                    <th class="branch">Chi nhánh</i></th>
                    <th class="deletedStatus">Trạng thái xoá</i></th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
        <div class="info-blocks">
            <fieldset>
                <legend>Ngày bắt đầu</legend>
                <input type="date" name="startingDate" required>
            </fieldset>
            <div class="form_text-input_err-message"></div>
        </div>
        <div class="info-blocks">
            <fieldset>
                <legend>Ngày cuối cùng</legend>
                <input type="date" name="endingDate" value="" required>
                <script>document.querySelector('.info-blocks input[name=endingDate]').value = new Date()</script>
            </fieldset>
            <div class="form_text-input_err-message"></div>
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
<script type="application/javascript" src="${pageContext.request.contextPath}/js/report-for-employee-activities.js"></script>
</body>
</html>