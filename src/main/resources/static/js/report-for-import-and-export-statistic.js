function GeneralMethodsHandler() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function ReportHandler() {
    const pdfFilesExporter = new PdfFilesExportation();
    const previewInfoContainer = 'div.preview-table-container';
    const fetchingConfigObject = {
        previewInfoContainer: previewInfoContainer,
        tablePreviewTitle: 'Tổng hợp nhập xuất',
        fetchDataAction: "/service/v1/branch/import-and-export-statistic",
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
        moreFeatures: () => {
            const _this = fetchingConfigObject;
            const formattedTotalImport = VNDCurrencyFormatEngine(_this.usefulVariablesStorage.totalImport, false);
            const formattedTotalExport = VNDCurrencyFormatEngine(_this.usefulVariablesStorage.totalExport, false);
            //--Prepare data for preview-statistic.
            _this.statisticComponents = [
                `<div class="preview-table-container_statistic">
                    <div><b>Tổng nhập</b>: ${formattedTotalImport} VND</div>
                    </br>
                    <div><b>Tổng xuất</b>: ${formattedTotalExport} VND</div>
                </div>`,
            ];
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
    GeneralMethodsHandler();
    await ReportHandler();
})();