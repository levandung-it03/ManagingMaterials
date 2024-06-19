<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
<div id="header">
    <c:if test="${userInfo.role.getJavaRole() == 'company'}">
        <style>
            .center-page_list {
                width: 100%!important;
            }
        </style>
    </c:if>
    <div class="header-wrapper center">
        <ul id="nav" class="center">
            <li><a href="/${userInfo.role.getJavaRole()}/employee/manage-employee">NHÂN VIÊN</a></li>
            <li><a href="/${userInfo.role.getJavaRole()}/supply/manage-supply">VẬT TƯ</a></li>
            <li><a href="/${userInfo.role.getJavaRole()}/warehouse/manage-warehouse">KHO</a></li>
            <li>
                <a href="">
                    LẬP PHIẾU
                    <i class="fa-solid fa-angle-down"></i>
                </a>
                <div>
                    <ul class="subnav">
                        <li><a href="/${userInfo.role.getJavaRole()}/order/manage-order">ĐƠN ĐẶT HÀNG</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/supplies-importation/manage-supplies-importation">PHIẾU NHẬP</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/supplies-exportation/manage-supplies-exportation">PHIẾU XUẤT</a></li>
                    </ul>
                </div>
            </li>
            <li>
                <a href="">
                    BÁO CÁO
                    <i class="fa-solid fa-angle-down"></i>
                </a>
                <div>
                    <ul class="subnav">
                        <li><a href="/${userInfo.role.getJavaRole()}/employee/report-for-employee">DANH SÁCH NHÂN VIÊN</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/supply/report-for-supply">DANH SÁCH VẬT TƯ</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/supply/report-for-detail-supplies-interact-info">CHI TIẾT NHẬP XUẤT</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/order/report-for-order-dont-have-import">ĐƠN HÀNG KHÔNG CÓ PHIẾU NHẬP</a></li>
                        <li><a href="/${userInfo.role.getJavaRole()}/employee/report-for-employee-activities">HOẠT ĐỘNG NHÂN VIÊN</a></li>
                        <li><a href="#">TỔNG HỢP NHẬP XUẤT</a></li>
                    </ul>
                </div>
            </li>
        </ul>
        <div class="avatar">
            <span class="mock-avatar center">${userInfo.fullName.charAt(0)}</span>
            <!-- Hiển thị khung thông tin tài khoản -->
            <div class="account-wrapper">
                <div class="account-info">
                    <div class="info center">
                        <span id="employeeId">Mã nhân viên: ${userInfo.employeeId}</span>
                        <span id="full-name">Họ tên: ${userInfo.fullName}</span>
                        <span id="branch">Chi Nhanh: ${userInfo.branch}</span>
                        <span id="role">Vai trò: ${userInfo.role}</span>
                    </div>
                </div>
                <div class="account-btn">
                    <form method="POST" action="/service/v1/auth/logout">
                        <button value="userInfo.employeeId">
                            <i class="fa-solid fa-right-from-bracket"></i>
                            Đăng xuất
                        </button>
                    </form>
                    <script>
                        document.querySelector(".account-btn form").onsubmit = (e) => (confirm("Xác nhận đăng xuất?") === true);
                    </script>
                    <a href="www.facebook.com/lvd11122003">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        Báo cáo
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>