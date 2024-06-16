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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report-for-detail-supply-info.css">
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
    <div class="center-page_more-info">
        <div class="more-info-title">
            <span>Báo cáo chi tiết số lượng - trị giá hàng nhập hoặc xuất</span>
        </div>
        <div class="info-blocks">
            <fieldset>
                <legend>Ngày bắt đầu</legend>
                <select name="ticketsType">
                    <option value="XUAT">XUAT</option>
                    <option value="NHAP">NHAP</option>
                </select>
            </fieldset>
            <div class="form_text-input_err-message"></div>
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
<script type="application/javascript" src="${pageContext.request.contextPath}/js/report-for-detail-supply-info.js"></script>
</body>
</html>