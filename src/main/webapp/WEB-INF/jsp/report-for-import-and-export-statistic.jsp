<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Report For Detail Supplies</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
        integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"/>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report-for-import-and-export-statistic.css">
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
  <div class="center-page_more-info">
    <div class="more-info-title">
      <span>Báo cáo Tổng hợp Nhập Xuất</span>
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
        <legend>Ngày kết thúc</legend>
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
<script type="application/javascript" src="${pageContext.request.contextPath}/js/report-for-import-and-export-statistic.js"></script>
</body>
</html>