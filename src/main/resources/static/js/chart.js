function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

const rangeValues = {
  min: 100000,
  max: 1000000,
};

var monthlyImport = [];
var monthlyExport = [];
for (var i = 1; i <= 12; i++) {
  monthlyImport.push({
    x: i,
    y: getRandomInt(100000, 1000000),
  });
  monthlyExport.push({
    x: i,
    y: getRandomInt(100000, 1000000),
  });
}

const selectingYear = 2023;
const chartName = `Year ${selectingYear} Monthly Import and Export Totals`;

// Monthly Comparison Chart
new Chart("monthly-comparison-chart", {
  type: "bar",
  data: {
    labels: monthlyImport.map((item) => `Tháng ${item.x}`),
    datasets: [
      {
        label: "Tỏng Nhập",
        backgroundColor: "#CDE8E5",
        data: monthlyImport.map((item) => item.y),
      },
      {
        label: "Tổng Xuất",
        backgroundColor: "#4D869C",
        data: monthlyExport.map((item) => item.y),
      },
    ],
  },
  options: {
    legend: {
      display: true,
    },
    scales: {
      xAxes: [
        {
          scaleLabel: {
            display: false,
            labelString: "Tháng",
          },
          ticks: {
            min: 1,
            max: 12,
          },
        },
      ],
      yAxes: [
        {
          ticks: { min: rangeValues.min, max: rangeValues.max },
          scaleLabel: {
            display: true,
            labelString: "VND",
          },
        },
      ],
    },
  },
});

// Trend Comparison Chart (Example data, replace with actual data)
var suppliesTrendData = {
  labels: ["Quạt", "Điều hòa", "TV", "Tủ lạnh", "Lò nướng"],
  datasets: [
    {
      label: "Nhập",
      backgroundColor: "#003C43",
      data: [
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
      ],
    },
    {
      label: "Xuất",
      backgroundColor: "#77B0AA",
      data: [
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
        getRandomInt(100, 1000),
      ],
    },
  ],
};

new Chart("trend-comparison-chart", {
  type: "bar",
  data: suppliesTrendData,
  options: {
    scales: {
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: "Vật tư",
          },
        },
      ],
      yAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: "Số lượng",
          },
        },
      ],
    },
  },
});

function sum(arr) {
  let total = 0;
  for (const item in arr) {
    total += item;
  }
  return total;
}

const totalLeftOverOfSupplies = [
  getRandomInt(10, 100),
  getRandomInt(10, 100),
  getRandomInt(10, 100),
  getRandomInt(10, 100),
  getRandomInt(10, 100),
];
const percentageOfSupplies = [
  (totalLeftOverOfSupplies[0] / sum(totalLeftOverOfSupplies)) * 100,
  (totalLeftOverOfSupplies[1] / sum(totalLeftOverOfSupplies)) * 100,
  (totalLeftOverOfSupplies[2] / sum(totalLeftOverOfSupplies)) * 100,
  (totalLeftOverOfSupplies[3] / sum(totalLeftOverOfSupplies)) * 100,
  (totalLeftOverOfSupplies[4] / sum(totalLeftOverOfSupplies)) * 100,
];

// Inventory Percentage Chart (Example data, replace with actual data)
var inventoryPercentageData = {
  labels: ["Quạt", "Điều hòa", "TV", "Tủ lạnh", "Lò nướng"],
  datasets: [
    {
      label: "Percentage",
      backgroundColor: ["#FF8080", "#A5DD9B", "#83A2FF", "#F4E869", "#FEBBCC"],
      data: percentageOfSupplies,
    },
  ],
};

new Chart("inventory-percentage-chart", {
  type: "pie",
  data: inventoryPercentageData,
  options: {},
});

// Example of populating card data - replace with actual data fetching logic
document.getElementById("total-supplies").innerText = "100"; // Replace with actual total supplies
document.getElementById("total-revenue").innerText = "$500,000"; // Replace with actual total revenue
document.getElementById("total-active-employees").innerText = "50"; // Replace with actual active employees count
document.getElementById("total-export-orders").innerText = "120"; // Replace with actual export orders count
document.getElementById("pending-import-orders").innerText = "10"; // Replace with actual pending import orders count
