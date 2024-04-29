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
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manage-supply.css">--%>
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
    
</div>
<script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<%--<script type="application/javascript" src="${pageContext.request.contextPath}/js/manage-supply.js"></script>--%>
</body>
</html>