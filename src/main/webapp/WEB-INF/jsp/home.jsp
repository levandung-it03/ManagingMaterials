<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
</head>

<body>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
    <c:if test="${errorMessage != null}">
        <div class="error-service-message">
            <span>${errorMessage}</span>
            <i id="error-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
        </div>
    </c:if>
    <p>Username: ${userInfo.username}</p>
    <p>ChiNhanh: ${userInfo.branch}</p>
    <p>MaNV: ${userInfo.employeeId}</p>
    <p>HoTen: ${userInfo.fullName}</p>
    <p>Nhom: ${userInfo.role}</p>
</body>

</html>