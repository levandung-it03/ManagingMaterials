<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
<div id="header">
    <div class="header-wrapper center">
        <ul id="nav" class="center">
            <li><a href="/branch/employee/manage-employee">NHÂN VIÊN</a></li>
            <li><a href="">VẬT TƯ</a></li>
            <li><a href="/branch/warehouse/manage-warehouse">KHO</a></li>
            <li>
                <a href="">
                    LẬP PHIẾU
                    <i class="fa-solid fa-angle-down"></i>
                </a>
                <div>
                    <ul class="subnav">
                        <li><a href="#">ĐƠN ĐẶT HÀNG</a></li>
                        <li><a href="#">PHIẾU NHẬP</a></li>
                        <li><a href="#">PHIẾU XUẤT</a></li>
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
                        <li><a href="#">DANH SÁCH NHÂN VIÊN</a></li>
                        <li><a href="#">DANH SÁCH VẬT TƯ</a></li>
                        <li><a href="#">CHI TIẾT NHẬP XUẤT</a></li>
                        <li><a href="#">ĐƠN HÀNG KHÔNG CÓ PHIẾU NHẬP</a></li>
                        <li><a href="#">HOẠT ĐỘNG NHÂN VIÊN</a></li>
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
                    <a href="">
                        <i class="fa-solid fa-right-from-bracket"></i>
                        Đăng xuất
                    </a>
                    <a href="">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        Báo cáo
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>