async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
    customizeSortingListEvent('div.center-page_list table');

    customizeSelectingTableInstanceEventInReportPages();
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function CustomizeExportationFileModules(roleForFetching) {
    const pdfFilesExporter = new PdfFilesExportation();
    const previewInfoContainer = 'div.preview-table-container';
    const fetchingConfigObject = {
        previewInfoContainer: previewInfoContainer,
        tablePreviewTitle: 'Danh sách hoạt động nhân viên',
        fetchDataAction: `/service/v1/${roleForFetching}/find-all-employee-activities-for-report`,
        usefulVariablesStorage: { statisticInfoOfEachMonth: {} },
        fieldObjects: [
            {cssName: "createdDate", utf8Name: "Ngày tạo"},
            {cssName: "ticketId", utf8Name: "Mã phiếu"},
            {cssName: "ticketType", utf8Name: "Loại phiếu"},
            {cssName: "customerFullName", utf8Name: "Tên khách hàng"},
            {cssName: "supplyName", utf8Name: "Tên vật tư"},
            {cssName: "suppliesQuantity", utf8Name: "Số lượng"},
            {cssName: "price", utf8Name: "Đơn giá"},
            {cssName: "totalPrice", utf8Name: "Trị giá"},
        ],
        rowFormattingEngine: (row) => {
            const _this = fetchingConfigObject;
            (function collectingDataToBuildHeadingAndStatisticLines() {
                const createdDate = new Date(row.createdDate);
                const createdMonth = (createdDate.getMonth() + 1) + "-" + createdDate.getFullYear();
                
                //--Each first time that reaching new-month-block.
                if (!Object.keys(_this.usefulVariablesStorage.statisticInfoOfEachMonth).includes(createdMonth)) {
                    _this.usefulVariablesStorage.statisticInfoOfEachMonth[createdMonth] = {
                        totalSuppliesQuantity: 0,
                        totalPrices: 0,
                        firstLineIndex: row.index,
                        lastLineIndex: null,
                    };
                }

                _this.usefulVariablesStorage.statisticInfoOfEachMonth[createdMonth].totalSuppliesQuantity
                    += Number.parseInt(row.suppliesQuantity.split(" ")[0]);
                _this.usefulVariablesStorage.statisticInfoOfEachMonth[createdMonth].totalPrices
                    += Number.parseFloat(row.totalPrice);
                _this.usefulVariablesStorage.statisticInfoOfEachMonth[createdMonth].lastLineIndex = row.index;
            })();

            return (function runningRowFormattingEngineMainLogics() {
                const customerFullName = row.customerFullName ? row.customerFullName : "Không";
                const price = VNDCurrencyFormatEngine(row.price, false);
                const totalPrice = VNDCurrencyFormatEngine(row.totalPrice, false);
                return `<tr index=${row.index} class="${row.ticketId}">
                        <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                        <td plain-value="${row.ticketId}" class="ticketId">${row.ticketId}</td>
                        <td plain-value="${row.ticketType}" class="ticketType"><b>${row.ticketType}</b></td>
                        <td plain-value="${customerFullName}" class="customerFullName">${customerFullName}</td>
                        <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                        <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                        <td plain-value="${price}" class="price">${price}</td>
                        <td plain-value="${totalPrice}" class="totalPrice">${totalPrice}</td>
                    </tr>`;
            })();
        },
        moreFeatures: () => {
            const _this = fetchingConfigObject;
            Object.entries(_this.usefulVariablesStorage.statisticInfoOfEachMonth).forEach(pair => {
                const statisticLine = `<tr>
                    <td plain-value="${pair[0]}" class="calculatedMonth">Tổng tháng: ${pair[0]}</td>
                        <td class="empty-separator">-</td>
                        <td class="empty-separator">-</td>
                        <td class="empty-separator">-</td>
                        <td class="empty-separator">-</td>
                        <td plain-value="${pair[1].totalSuppliesQuantity}" class="totalSuppliesQuantity">
                            Tổng số lượng: ${pair[1].totalSuppliesQuantity}
                        </td>
                        <td class="empty-separator">-</td>
                        <td plain-value="${VNDCurrencyFormatEngine(pair[1].totalPrices, false)}" class="totalPrices">
                            Tổng trị giá: ${VNDCurrencyFormatEngine(pair[1].totalPrices, false)}
                        </td>
                    </tr>`;
                const headerLine = `<tr style="background-color: var(--smoking-grey);">
                    <td plain-value="${pair[0]}" class="createdMonth">Tháng: ${pair[0]}</td>
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
        }
    };

    await pdfFilesExporter.loadAllNecessaryLibs()
        .then(() => {
            //--Customize clicking-preview-btn event.
            $('.report-supporting-buttons_preview').addEventListener("click", async e => {
                try {
                    //--Update data-info of preview-page continuously (may throw exception at .trim()).
                    const getValueInCellEngine = (className => $('.center-page_more-info tbody td.' + className).textContent.trim());
                    const employeeBodyDataCells = {
                        fullName: getValueInCellEngine('lastName') + " " + getValueInCellEngine('firstName'),
                        employeeId: getValueInCellEngine('employeeId'),
                        identifier: getValueInCellEngine('identifier'),
                        address: getValueInCellEngine('address'),
                        deletedStatus: getValueInCellEngine('deletedStatus'),
                    };
                    const branchValue = $('.table-tools .select-branch-to-search select').value;
                    const startingDateValue = $('.info-blocks input[name=startingDate]').value;
                    const endingDateValue = $('.info-blocks input[name=endingDate]').value;

                    if (!(branchValue
                        && startingDateValue
                        && endingDateValue
                        && (startingDateValue <= endingDateValue)
                    )) throw new Error("Invalid values");

                    //--Prepare data to fetch.
                    fetchingConfigObject.dataObject = {
                        employeeId: employeeBodyDataCells.employeeId,
                        startingDate: startingDateValue,
                        endingDate: endingDateValue,
                    }
                    //--Prepare data for preview-descriptions.
                    fetchingConfigObject.descriptionComponents = [
                        `<div class="preview-table-container_descriptions">
                            <span><b>Chi nhánh</b>: ${branchValue}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Ngày bắt đầu</b>: ${startingDateValue}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Ngày kết thúc </b>: ${endingDateValue}</span>
                        </div>`,
                        //--Empty line-space.
                        `<div class="preview-table-container_descriptions"><span></span></div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Mã nhân viên</b>: ${employeeBodyDataCells.employeeId}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Họ tên</b>: ${employeeBodyDataCells.fullName}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>CMND</b>: ${employeeBodyDataCells.identifier}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Địa chỉ</b>: ${employeeBodyDataCells.address}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Trạng thái xoá</b>: ${employeeBodyDataCells.deletedStatus}</span>
                        </div>`,
                    ];
                    //--Prepare data for preview-statistic.
                    fetchingConfigObject.statisticComponents = [];

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
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "employeeId",
            searchingValue: "",
            branch: $('.table-tools .select-branch-to-search select').value,
        },
        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-employee-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.employeeId}">
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.identifier}" class="identifier">${row.identifier}</td>
                <td plain-value="${row.lastName}" class="lastName">${row.lastName}</td>
                <td plain-value="${row.firstName}" class="firstName">${row.firstName}</td>
                <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                <td plain-value="${row.salary}" class="salary">${VNDCurrencyFormatEngine(row.salary)}</td>
                <td plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td plain-value="${row.deletedStatus}" class="deletedStatus">${row.deletedStatus}</td>
            </tr>`
    };

    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "người",
            callModulesOfExtraFeatures: async () => {
            }
        }
    );
    await ListComponent(searchingSupportingDataSource);
    await CustomizeExportationFileModules(roleForFetching);
})();