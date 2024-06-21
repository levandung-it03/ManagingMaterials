function GeneralMethodsHandler() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function ReportHandler(roleForFetching) {
    const pdfFilesExporter = new PdfFilesExportation();
    const previewInfoContainer = 'div.preview-table-container';
    const fetchingConfigObject = {
        previewInfoContainer: previewInfoContainer,
        tablePreviewTitle: 'Tổng hợp nhập xuất',
        // TODO: remove the context path prefix when transfer code to main project
        fetchDataAction: `/service/v1/${roleForFetching}/import-and-export-statistic`,
        usefulVariablesStorage: {
            totalImport: 0,
            totalExport: 0,
        },
        fieldObjects: [
            // TODO: rename CSS name for precise naming reference
            {cssName: "columnData", utf8Name: "Ngày"},
            {cssName: "columnData", utf8Name: "Nhập"},
            {cssName: "columnData", utf8Name: "Tỷ lệ nhập"},
            {cssName: "columnData", utf8Name: "Xuất"},
            {cssName: "columnData", utf8Name: "Tỷ lệ xuất"},
        ],
        rowFormattingEngine: (row) => {
            const _this = fetchingConfigObject;
            _this.usefulVariablesStorage.totalImport += row.supplyImport;
            _this.usefulVariablesStorage.totalExport += row.supplyExport;

            return (function runningRowFormattingEngineMainLogics() {
                //--Running row formatting engine main-logics.
                const formattedDate = new Date(row.date).toISOString().split('T')[0];
                const supplyImport = VNDCurrencyFormatEngine(row.supplyImport, false);
                const supplyExport = VNDCurrencyFormatEngine(row.supplyExport, false);
                const formattedImportPercentage = row.importPercentage.toFixed(2);
                const formattedExportPercentage = row.exportPercentage.toFixed(2);

                return `<tr index=${row.index} class="preview-row-data">
                            <td plain-value="${formattedDate}" class="columnData" style="text-align: left !important; padding-left: 10px !important;">${formattedDate}</td>
                            <td plain-value="${supplyImport}" class="columnData" style="text-align: left !important; padding-left: 10px !important;">${supplyImport}</td>
                            <td plain-value="${formattedImportPercentage}" class="columnData">${formattedImportPercentage}%</td>
                            <td plain-value="${supplyExport}" class="columnData" style="text-align: left !important; padding-left: 10px !important;">${supplyExport}</td>
                            <td plain-value="${formattedExportPercentage}" class="columnData">${formattedExportPercentage}%</td>
                        </tr>`;
            })();
        },
        moreFeatures: async () => {
            const result = await pdfFilesExporter.fetchDataForReporter(fetchingConfigObject);
            const tbody = document.createElement("tbody");
            tbody.innerHTML = result;

            const toInt = (str) => parseInt(str.replace(/,/g, ''), 10);
            var totalImport = 0;
            var totalExport = 0;
            const rows = tbody.querySelectorAll('tr');
            rows.forEach((row, index) => {
                const columns = row.querySelectorAll('td');
                columns.forEach((column, index) => {
                    if (index === 1) {
                        totalImport += toInt(column.innerText);
                    }
                    if (index == 3) {
                        totalExport += toInt(column.innerText);
                    }
                })
            })

            const formattedTotalImport = VNDCurrencyFormatEngine(totalImport, false);
            const formattedTotalExport = VNDCurrencyFormatEngine(totalExport, false);

            var lastRow = tbody.insertRow();
            lastRow.setAttribute("index", "last-row");
            lastRow.setAttribute("class", "preview-row-data");

            var firstColumn = lastRow.insertCell(0);
            firstColumn.setAttribute("plain-value", "Tổng cộng");
            firstColumn.setAttribute("class", "columnData");
            firstColumn.style.textAlign = "left";
            firstColumn.style.paddingLeft = "10px";
            firstColumn.innerHTML = "Tổng cộng";

            var secondColumn = lastRow.insertCell(1);
            secondColumn.setAttribute("plain-value", formattedTotalImport);
            secondColumn.setAttribute("class", "columnData");
            secondColumn.style.textAlign = "left";
            secondColumn.style.paddingLeft = "10px";
            secondColumn.innerHTML = formattedTotalImport + " VND";

            var thirdColumn = lastRow.insertCell(2);
            thirdColumn.setAttribute("plain-value", "");
            thirdColumn.setAttribute("class", "columnData");
            thirdColumn.style.textAlign = "left";
            thirdColumn.style.paddingLeft = "10px";
            thirdColumn.innerHTML = "";

            var fourthColumn = lastRow.insertCell(3);
            fourthColumn.setAttribute("plain-value", formattedTotalExport);
            fourthColumn.setAttribute("class", "columnData");
            fourthColumn.style.textAlign = "left";
            fourthColumn.style.paddingLeft = "10px";
            fourthColumn.innerHTML = formattedTotalExport + " VND";

            var fifthColumn = lastRow.insertCell(4);
            fifthColumn.setAttribute("plain-value", "");
            fifthColumn.setAttribute("class", "columnData");
            fifthColumn.style.textAlign = "left";
            fifthColumn.style.paddingLeft = "10px";
            fifthColumn.innerHTML = "";

            const exportTable = $('.exporting-table-css tbody');
            exportTable.innerHTML = tbody.innerHTML;
        },
    };

    await pdfFilesExporter.loadAllNecessaryLibs()
        .then(() => {
            // * Handle `report-supporting-buttons_preview` button click event
            $('.report-supporting-buttons_preview').addEventListener("click", async e => {
                try {
                    //--Update data-info of preview-page continuously (may throw exception at .trim()).
                    const beginDate = $('.info-blocks input[name = startingDate]').value;
                    const endDate = $('.info-blocks input[name = endingDate]').value;

                    if (!(beginDate && endDate && (beginDate <= endDate))) {
                        throw new Error("Invalid values");
                    }

                    //--Prepare data to fetch.
                    fetchingConfigObject.dataObject = {
                        startingDate: beginDate,
                        endingDate: endDate,
                    }

                    //--Prepare data for preview-descriptions.
                    fetchingConfigObject.descriptionComponents = [
                        `<div class="preview-table-container_descriptions">
                            <span><b>TỪ </b>${beginDate} ĐẾN ${endDate}</span>
                        </div>`,
                    ];

                    fetchingConfigObject.usefulVariablesStorage = {
                        totalImport: 0,
                        totalExport: 0,
                    };

                    //--Build preview table data.
                    await pdfFilesExporter.buildPreviewPages(fetchingConfigObject)
                        .then(() => {
                            //--Open preview-page after building page successfully.
                            $(previewInfoContainer).style.minHeight = $('html body').offsetHeight + "px";
                            $(previewInfoContainer).classList.remove("closed");
                            window.scrollTo(0, 0);

                            // --Customize clicking-pdf-exporting-btn event (inside preview-page).
                            $('.report-supporting-buttons_exporting-report').addEventListener("click", async e =>
                                pdfFilesExporter.exportToPdfFile(previewInfoContainer + ' table'));
                        })
                        .catch(err => console.log("Error when building-preview-page: " + err));
                } catch (err) {
                    //--Rejected or throw exceptions.
                    alert("Thông tin chưa đủ hoặc không đúng, vui lòng kiểm tra lại!");
                    console.log(err);
                }
            });
        })
        .catch(err => {
            alert("Có lỗi xảy ra khi xây dựng 'Bảng thông tin xem trước', vui lòng đợi thêm vài giây để tài nguyên kịp thời tải.");
            console.log(err);
        });
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    GeneralMethodsHandler();
    await ReportHandler(roleForFetching);
})();