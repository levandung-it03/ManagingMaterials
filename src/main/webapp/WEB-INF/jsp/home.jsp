<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <title>Home</title>

    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css"
            integrity="sha512-SzlrxWUlpfuzQ+pcUCosxcglQRNAq/DZjVsC0lE40xsADsfeQoEypE+enwcOiGjk/bSuGGKHEyjSoQ1zVisanQ=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
    />

    <!-- home.css -->
    <link
            rel="stylesheet"
            type="text/css"
            href="${pageContext.request.contextPath}/css/base.css"
    />

    <!-- header.css -->
    <link
            rel="stylesheet"
            type="text/css"
            href="${pageContext.request.contextPath}/css/header.css"
    />

    <!-- statistic.css -->
    <link
            rel="stylesheet"
            type="text/css"
            href="${pageContext.request.contextPath}/css/statisitc.css"
    />
</head>

<body>
<!-- * Original Error Message Handler -->
<div id="message-block">
    <c:if test="${errorMessage != null}">
        <div class="error-service-message">
            <span>${errorMessage}</span>
            <i id="error-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
        </div>
    </c:if>
</div>

<!-- * Original Header Section -->
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div class="greeting-container">
    <h2>Ứng dụng quản lí vật tư</h2>
</div>

<div class="statistic-analysis-container">
    <!-- * Summary statistic data cards -->
    <div id="summary-statistic-data" class="summary-statistic-data">
        <div class="card">
            <h3>Tổng số vật tư</h3>
            <p id="total-supplies">${numberOfSupply}</p>
        </div>
        <div class="card">
            <h3>Tổng doanh thu chi nhánh</h3>
            <p id="total-revenue">${totalRevenue}</p>
        </div>
        <div class="card">
            <h3>Tổng số nhân viên</h3>
            <p id="total-active-employees">${numberOfEmployee}</p>
        </div>
        <div class="card">
            <h3>Tổng số đơn xuất vật tư</h3>
            <p id="total-export-orders">${numberOfExportation}</p>
        </div>
        <div class="card">
            <h3>Số đơn đặt chưa được nhập</h3>
            <p id="pending-import-orders">${numberOfOrderWithoutImportation}</p>
        </div>
    </div>

    <!-- * Statistic charts -->
    <div class="chart-container">
        <h3>Tổng hợp nhập xuất trong năm 2023</h3>
        <div id=""></div>
        <div class="chartBox">
            <canvas id="monthly-comparison-chart"></canvas>
        </div>
    </div>
    <div class="chart-container">
        <h3>Xu hướng nhập và xuất của từng vật tư</h3>
        <div class="chartBox">
            <canvas id="trend-comparison-chart"></canvas>
        </div>
    </div>
    <div class="chart-container">
        <h3>Tỉ lệ tồn kho của các vật tư</h3>
        <div class="chartBox" style="width: 80%; margin: 0 auto">
            <canvas id="inventory-percentage-chart" height="300" width="600"></canvas>
        </div>
    </div>
</div>
</body>

<%--  <script type="application/javascript" src="${pageContext.request.contextPath}/js/chart.js"></script>--%>
<script>
    async function fetchData(resource) {
        const url = window.location.origin + "/service/v1/${userInfo.role.getJavaRole()}" + resource;
        console.log("url:", url);
        const payload = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
        };
        let responseData = null;
        await fetch(url, payload)
            .then(response => {
                if (!response.ok) {
                    throw new Error("An error occurs");
                }
                return response.json();
            })
            .then(responseObject => {
                responseData = responseObject;
            })
            .catch(error => {
                console.error("Error:", error);
            });
        return responseData;
    }

    async function buildMonthlyTotalImportAndExportChart() {
        const resource = "/total-monthly-import";
        const chartSourceData = await fetchData(resource);
        const monthlyImport = chartSourceData.monthlyImport;
        const monthlyExport = chartSourceData.monthlyExport;
        const minRangeValue = Math.min(...monthlyImport, ...monthlyExport);
        const maxRangeValue = Math.max(...monthlyImport, ...monthlyExport);

        new Chart("monthly-comparison-chart", {
            type: "bar",
            data: {
                labels: monthlyImport.map((_, index) => "Tháng " + (+index + 1)),
                datasets: [
                    {
                        label: "Tổng Nhập",
                        backgroundColor: "#CDE8E5",
                        data: monthlyImport,
                    },
                    {
                        label: "Tổng Xuất",
                        backgroundColor: "#4D869C",
                        data: monthlyExport,
                    },
                ],
            },
            options: {
                plugins: {
                    legend: {
                        display: true,
                        labels: {
                            font: {
                                size: 14
                            }
                        }
                    },
                },
                scales: {
                    x: {
                        title: {
                            display: false,
                        },
                        ticks: {
                            min: 1,
                            max: 12,
                        },
                    },
                    y: {
                        title: {
                            display: true,
                            text: "VND",
                        },
                        ticks: {
                            min: minRangeValue,
                            max: maxRangeValue,
                        },
                    },
                },
            },
        });
    }

    async function buildSupplyTrendChart() {
        const resource = "/supply-trend";
        const chartSourceData = await fetchData(resource);

        const suppliesTrendData = {
            labels: chartSourceData.supplies,
            datasets: [
                {
                    label: "Nhập",
                    backgroundColor: "#003C43",
                    data: chartSourceData.supplyTotalImport,
                },
                {
                    label: "Xuất",
                    backgroundColor: "#77B0AA",
                    data: chartSourceData.supplyTotalExport,
                },
            ],
        };

        new Chart("trend-comparison-chart", {
            type: "bar",
            data: suppliesTrendData,
            options: {
                scales: {
                    x: {
                        scaleLabel: {
                            display: true,
                            labelString: "Vật tư",
                        },
                    },
                    y: {
                        scaleLabel: {
                            display: true,
                            labelString: "Số lượng",
                        },
                    },
                },
                plugins: {
                    legend: {
                        labels: {
                            font: {
                                size: 14
                            }
                        }
                    }
                }
            },
        });
    }

    async function buildInventoryPercentageChart() {
        const resource = "/inventory";
        const chartSourceData = await fetchData(resource);

        const inventoryPercentageData = {
            labels: chartSourceData.supplies,
            datasets: [
                {
                    label: "Percentage",
                    data: chartSourceData.percentages,
                },
            ],
        };

        new Chart("inventory-percentage-chart", {
            type: "pie",
            data: inventoryPercentageData,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        labels: {
                            font: {
                                size: 14
                            }
                        }
                    }
                }
            },
        });
    }

    async function main() {
        await buildMonthlyTotalImportAndExportChart();
        await buildSupplyTrendChart();
        await buildInventoryPercentageChart();
    }

    main();
</script>
</html>
