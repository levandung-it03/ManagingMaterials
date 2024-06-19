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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report-for-employee.css">
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
                    <b>Báo cáo toàn bộ nhân viên </b>
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
                        <th id="employeeId">Mã<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="identifier">CMND<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="lastName">Họ<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="firstName">Tên<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="birthday">Ngày sinh<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="address">Địa chỉ<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="salary">Lương<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="branch">Chi nhánh<i class="fa-solid fa-arrow-down-a-z"></i></th>
                        <th id="deletedStatus">Trạng thái xoá<i class="fa-solid fa-arrow-down-a-z"></i></th>
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
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/report-for-employee.js"></script>
</body>
</html>