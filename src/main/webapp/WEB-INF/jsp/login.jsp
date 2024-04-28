<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en" style=" background-image: url('${pageContext.request.contextPath}/img/form_background.jpeg'); background-size: cover;">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
          integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>

<body>
    <div id="message-block">
        <c:if test="${errorMessage != null}">
            <div class="error-service-message">
                <span>${errorMessage}</span>
                <i id="error-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
            </div>
        </c:if>
    </div>
    <form method="POST" action="/service/v1/auth/authenticate" modelAttribute="authObject">
        <span id="form-title">Đăng nhập</span>
        <div class="form-input" id="branch">
            <label for="branch">Chọn chi nhánh</label>
            <select name="branch">
                <option value="CN1">Chi nhánh 1</option>
                <option value="CN2">Chi nhánh 2</option>
            </select>
        </div>
        <div class="form-input" id="username">
            <label for="username">Tên đăng nhập</label>
            <input name="username" type="text" required/>
            <div class="form_text-input_err-message"></div>
        </div>

        <div class="form-input" id="password">
            <label for="password">Mật khẩu</label>
            <span id="forgot-pass">
                <a href="/public/forgot-password">Quên mật khẩu?</a>
            </span>
            <input name="password" type="password" required/>
            <div class="form_text-input_err-message"></div>
            <div class="password_toggle-hidden">
                <i id="password" class="show-pass fa-solid fa-eye"></i>
                <i id="password" class="hide-pass hidden fa-regular fa-eye-slash"></i>
            </div>
        </div>
        <input type="submit" value="Đăng nhập">
    </form>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
</body>

</html>