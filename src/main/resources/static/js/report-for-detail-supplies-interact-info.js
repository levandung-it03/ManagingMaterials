function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function CustomizeExportationFileModules(roleForFetching) {
    const pdfFilesExporter = new PdfFilesExportation();
    const previewInfoContainer = 'div.preview-table-container';
    const fetchingConfigObject = {
        previewInfoContainer: previewInfoContainer,
        tablePreviewTitle: 'Danh sách chi tiết số lượng - trị giá hàng nhập hoặc xuất',
        fetchDataAction: `/service/v1/${roleForFetching}/find-tickets-for-detail-supplies-report`,
        fieldObjects: [
            {cssName: "supplyName", utf8Name: "Tên vật tư"},
            {cssName: "totalSuppliesQuantity", utf8Name: "Tổng số lượng"},
            {cssName: "totalPrices", utf8Name: "Tổng trị giá"},
        ],
        rowFormattingEngine: (row) => {
            const _this = fetchingConfigObject;
            (function collectingDataToBuildHeadingAndStatisticLines() {
                //--Each first time that reaching new-month-block.
                if (!Object.keys(_this.usefulVariablesStorage.statisticInfoOfEachMonth).includes(row.month)) {
                    _this.usefulVariablesStorage.statisticInfoOfEachMonth[row.month] = {
                        totalSuppliesQuantity: 0,
                        totalPrices: 0,
                        firstLineIndex: row.index,
                        lastLineIndex: null,
                    };
                }
                _this.usefulVariablesStorage.statisticInfoOfEachMonth[row.month].lastLineIndex = row.index;
                _this.usefulVariablesStorage.statisticInfoOfEachMonth[row.month].totalSuppliesQuantity
                    += Number.parseInt(row.totalSuppliesQuantity);
                _this.usefulVariablesStorage.statisticInfoOfEachMonth[row.month].totalPrices
                    += Number.parseFloat(row.totalPrices);
                _this.usefulVariablesStorage.statisticInfoOfAllMonth.totalPrices += row.totalPrices;
            })();

            return (function runningRowFormattingEngineMainLogics() {
                //--Running row formatting engine main-logics.
                const totalPrices = VNDCurrencyFormatEngine(row.totalPrices, false);
                return `<tr index=${row.index} class="${row.month}">
                    <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                    <td plain-value="${row.totalSuppliesQuantity}" class="totalSuppliesQuantity">${row.totalSuppliesQuantity}</td>
                    <td plain-value="${totalPrices}" class="totalPrices">${totalPrices}</td>
                </tr>`;
            })();
        },
        moreFeatures: () => {
            const _this = fetchingConfigObject;
            (function buildingMonthHeaderAndStatisticOfEachMonthBlock() {
                Object.entries(_this.usefulVariablesStorage.statisticInfoOfEachMonth).forEach(pair => {
                    const statisticLine = `<tr>
                        <td plain-value="${pair[0]}" class="calculatedMonth">Tổng tháng: ${pair[0]}</td>
                            <td plain-value="${pair[1].totalSuppliesQuantity}" class="totalSuppliesQuantity">
                                Tổng số lượng: ${pair[1].totalSuppliesQuantity}
                            </td>
                            <td plain-value="${VNDCurrencyFormatEngine(pair[1].totalPrices, false)}" class="totalPrices">
                                Tổng trị giá: ${VNDCurrencyFormatEngine(pair[1].totalPrices, false)}
                            </td>
                        </tr>`;
                    const headerLine = `<tr style="background-color: var(--smoking-grey);">
                        <td plain-value="${pair[0]}" class="month">Tháng: ${pair[0]}</td>
                    </tr>`;

                    //--Get first-line in current-month-block.
                    const firstLine = $(`${_this.previewInfoContainer} table tbody tr[index="${pair[1].firstLineIndex}"]`);
                    //--If there's just 1 line of current-month-block.
                    if (pair[1].firstLineIndex === pair[1].lastLineIndex) {
                        firstLine.outerHTML = headerLine + firstLine.outerHTML + statisticLine;
                    } else {
                        //--Get last-line in current-month-block.
                        const lastLine = $(`${_this.previewInfoContainer} table tbody tr[index="${pair[1].lastLineIndex}"]`);
                        firstLine.outerHTML = headerLine + firstLine.outerHTML;
                        lastLine.outerHTML += statisticLine;
                    }
                });
            })();

            //--Prepare data for preview-statistic.
            _this.statisticComponents = [
                `<div class="preview-table-container_statistic">
                    <span><b>Tổng toàn bộ trị giá </b>: ${
                        VNDCurrencyFormatEngine(_this.usefulVariablesStorage.statisticInfoOfAllMonth.totalPrices, true)
                    }</span>
                </div>`,
            ];
        },
    };

    await pdfFilesExporter.loadAllNecessaryLibs()
        .then(() => {
            //--Customize clicking-preview-btn event.
            $('.report-supporting-buttons_preview').addEventListener("click", async e => {
                try {
                    //--Update data-info of preview-page continuously (may throw exception at .trim()).
                    const ticketsTypeValue = $('.info-blocks select[name=ticketsType]').value.toUpperCase().trim();
                    const startingDateValue = $('.info-blocks input[name=startingDate]').value;
                    const endingDateValue = $('.info-blocks input[name=endingDate]').value;

                    if (!(ticketsTypeValue
                        && startingDateValue
                        && endingDateValue
                        && (startingDateValue <= endingDateValue)
                        && (new Date(endingDateValue) <= new Date())
                    )) throw new Error("Invalid values");

                    //--Prepare data to fetch.
                    fetchingConfigObject.dataObject = {
                        ticketsType: ticketsTypeValue,
                        startingDate: startingDateValue,
                        endingDate: endingDateValue,
                    }
                    //--Prepare data for preview-descriptions.
                    fetchingConfigObject.descriptionComponents = [
                        `<div class="preview-table-container_descriptions">
                            <span><b>Loại phiếu</b>: ${ticketsTypeValue}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Ngày bắt đầu</b>: ${startingDateValue}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Ngày kết thúc </b>: ${endingDateValue}</span>
                        </div>`,
                    ];
                    //--Re-initialize each time open preview page.
                    fetchingConfigObject.usefulVariablesStorage = {
                        statisticInfoOfEachMonth: {},
                        statisticInfoOfAllMonth: { totalPrices: 0 }
                    };

                    //--Build preview table data.
                    await pdfFilesExporter.buildPreviewPages(fetchingConfigObject)
                        .then(() => {
                            //--Open preview-page after building page successfully.
                            $(previewInfoContainer).style.minHeight = $('html body').offsetHeight + "px";
                            $(previewInfoContainer).classList.remove("closed");
                            window.scrollTo(0, 0);

                            //--Customize clicking-pdf-exporting-btn event (inside preview-page).
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
    GeneralMethods();
    await CustomizeExportationFileModules(roleForFetching);
})();