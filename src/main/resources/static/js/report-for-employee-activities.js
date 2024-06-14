const fakeData = [];

async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
    customizeSortingListEvent('div.center-page_list table');

    customizeSelectingTableInstanceEvent();
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function CustomizeExportationFileModules() {
    const pdfFilesExporter = new PdfFilesExportation();
    const previewInfoContainer = 'div.preview-table-container';
    const fetchingConfigObject = {
        previewInfoContainer: previewInfoContainer,
        tablePreviewTitle: 'Danh sách nhân viên',
        fetchDataAction: "/service/v1/branch/find-employee-by-values",
        dataObject: {
            //--If page-number is "0", it's means that we will search all the list without pagination.
            currentPage: 0,
            searchingField: "employeeId",
            searchingValue: "",
            branch: $('.table-tools .select-branch-to-search select').value,
        },
        fieldObjects: [
            { cssName: "employeeId", utf8Name: "Mã" },
            { cssName: "identifier", utf8Name: "CMND" },
            { cssName: "lastName", utf8Name: "Họ" },
            { cssName: "firstName", utf8Name: "Tên" },
            { cssName: "birthday", utf8Name: "Ngày sinh" },
            { cssName: "address", utf8Name: "Địa chỉ" },
            { cssName: "salary", utf8Name: "Lương" },
            { cssName: "branch", utf8Name: "Chi nhánh" },
            { cssName: "deletedStatus", utf8Name: "Đã xoá" },
        ],
        rowFormattingEngine: (row) => `
            <tr id="${row.employeeId}">
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.identifier}" class="identifier">${row.identifier}</td>
                <td plain-value="${row.lastName}" class="lastName">${row.lastName}</td>
                <td plain-value="${row.firstName}" class="firstName">${row.firstName}</td>
                <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                <td plain-value="${row.salary}" class="salary">${salaryFormattingEngine(row.salary, false)}</td>
                <td plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td plain-value="${row.deletedStatus}" class="deletedStatus">${row.deletedStatus}</td>
            </tr>
        `,
        fakeData: fakeData,
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
                    const ticketsTypeValue = $('.info-blocks select[name=ticketsType]').value;
                    const startingDateValue = $('.info-blocks input[name=startingDate]').value;
                    const endingDateValue = $('.info-blocks input[name=endingDate]').value;

                    if (!(branchValue
                        && ticketsTypeValue
                        && startingDateValue
                        && endingDateValue
                        && (startingDateValue <= endingDateValue)
                    )) throw new Error("Invalid values");

                    //--Prepare data for preview-descriptions.
                    fetchingConfigObject.descriptionComponents = [
                        `<div class="preview-table-container_descriptions">
                            <span>Chi nhánh: ${branchValue}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span>Loại phiếu: ${ticketsTypeValue == "XUAT" ? "Phiếu Xuất" : "Phiếu Nhập"}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Ngày bắt đầu</b>: ${new Date(startingDateValue)} - <b>Ngày kết thúc </b>: ${new Date(endingDateValue)}</span>
                        </div>`,
                        //--Empty line-space.
                        `<div class="preview-table-container_descriptions"><span></span></div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Mã nhân viên</b>: ${employeeBodyDataCells.employeeId}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Họ tên</b>: ${employeeBodyDataCells.fullName} _ <b>CMND</b>: ${employeeBodyDataCells.identifier}</span>
                        </div>`,
                        `<div class="preview-table-container_descriptions">
                            <span><b>Mã nhân viên</b>: ${employeeBodyDataCells.address}</span>
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
                    //--Rejected or throwed exceptions.
                    alert("Thông tin chưa đủ hoặc không đúng, vui lòng kiểm tra lại!");
                    console.log(err);
                };
            });
        })
        .catch(err => {
            alert("Có lỗi xảy ra khi xây dựng 'Bảng thông tin xem trước', vui lòng đợi thêm vài giây để tài nguyên kịp thời tải.");
            console.log(err);
        });
}

// (async function main() {
//     const searchingSupportingDataSource = {
//         //--Initialize field-values for firstly fetch action.
//         data: {
//             currentPage: 1,
//             objectsQuantity: 0,
//             searchingField: "employeeId",
//             searchingValue: "",
//             branch: $('.table-tools .select-branch-to-search select').value,
//         },
//         //--Main fields for searching-action.
//         tableBody: $('div.center-page_list table tbody'),
//         fetchDataAction: "/service/v1/branch/find-employee-by-values",
//         rowFormattingEngine: (row) => `
//             <tr id="${row.employeeId}">
//                 <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
//                 <td plain-value="${row.identifier}" class="identifier">${row.identifier}</td>
//                 <td plain-value="${row.lastName}" class="lastName">${row.lastName}</td>
//                 <td plain-value="${row.firstName}" class="firstName">${row.firstName}</td>
//                 <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
//                 <td plain-value="${row.address}" class="address">${row.address}</td>
//                 <td plain-value="${row.salary}" class="salary">${salaryFormattingEngine(row.salary)}</td>
//                 <td plain-value="${row.branch}" class="branch">${row.branch}</td>
//                 <td plain-value="${row.deletedStatus}" class="deletedStatus">${row.deletedStatus}</td>
//             </tr>`
//     };

//     GeneralMethods();
//     CustomizeFetchingActionSpectator(
//         searchingSupportingDataSource,
//         {
//             tableLabel: "người",
//             callModulesOfExtraFeatures: async () => {}
//         }
//     );
//     await ListComponent(searchingSupportingDataSource);
//     await CustomizeExportationFileModules();
// })();

(async function main() {
    const dataRow = (id) => `
        <tr id="${id}">
            <td plain-value="${id}" class="employeeId">${id}</td>
            <td plain-value="038203032576" class="identifier">038203032576</td>
            <td plain-value="Phạm Đỗ Hùng Nguyễn Lý Thanh" class="lastName">Phạm Đỗ Hùng Nguyễn Lý Thanh</td>
            <td plain-value="Khương" class="firstName">Khương</td>
            <td plain-value="2003-11-12" class="birthday">2003-11-12</td>
            <td plain-value="Thành phố Hồ chí Minh" class="address">Thành phố Hồ chí Minh</td>
            <td plain-value="100000000" class="salary">100,000,000VND</td>
            <td plain-value="ChiNhanh1" class="branch">ChiNhanh1</td>
            <td plain-value="0" class="deletedStatus">0</td>
        </tr>`;
    const bodyTag = $('div.center-page_list table tbody')
    const buildFakeTable = (tableBody, dataRow) => {
        for (var i = 1; i <= 30; i++)   fakeData.push(dataRow(i));
        tableBody.innerHTML = fakeData.join("");
    }
    buildFakeTable(bodyTag, dataRow);
    customizeSelectingTableInstanceEventInReportPages();
    CustomizeExportationFileModules();
})();