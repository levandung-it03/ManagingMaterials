
async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
    customizeSortingListEvent();
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function CustomizeExportationFileModules() {
    const pdfFilesExporter = new PdfFilesExportation();
    const tablePreviewContainerSelector = 'div.preview-table-container';
    const fetchingConfigObject = {
        tablePreviewContainerSelector: tablePreviewContainerSelector,
        fetchDataAction: "/service/v1/branch/find-employee-by-values",
        dataObject: {
            //--If page-number is "0", it's means that we will search all the list without pagination.
            currentPage: 0,
            searchingField: "employeeId",
            searchingValue: "",
            branch: $('.table-tools .select-branch-to-search select').value,
        },
        fieldObjects: [
            {cssName: "employeeId", utf8Name: "Mã"},
            {cssName: "identifier", utf8Name: "CMND"},
            {cssName: "lastName", utf8Name: "Họ"},
            {cssName: "firstName", utf8Name: "Tên"},
            {cssName: "birthday", utf8Name: "Ngày sinh"},
            {cssName: "address", utf8Name: "Địa chỉ"},
            {cssName: "salary", utf8Name: "Lương"},
            {cssName: "branch", utf8Name: "Chi nhánh"},
            {cssName: "deletedStatus", utf8Name: "Đã xoá"},
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
            </tr>`
    };

    await pdfFilesExporter.loadAllNecessaryLibs()
        .then(() => {
            //--Build preview table data.
            pdfFilesExporter.buildPreviewPages(fetchingConfigObject)

            //--Customize clicking-preview-btn event.
            $('.report-supporting-buttons_preview').addEventListener("click", e =>
                $(tablePreviewContainerSelector).classList.remove("closed"));

            //--Customize clicking-pdf-exporting-btn event.
            $('.report-supporting-buttons_exporting-report').addEventListener("click", e =>
                pdfFilesExporter.exportToPdfFile(tablePreviewContainerSelector + ' table'));

            $('.preview-table-container').addEventListener("click", e => {
                if (e.target.classList.contains('preview-table-container')) {
                    e.target.classList.add('closed');
                }
            });
        })
        .catch(err => {
            alert("Có lỗi xảy ra khi xây dựng 'Bảng thông tin xem trước', vui lòng đợi thêm vài giây để tài nguyên kịp thời tải.");
        });
}

(async function main() {
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
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-employee-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.employeeId}">
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.identifier}" class="identifier">${row.identifier}</td>
                <td plain-value="${row.lastName}" class="lastName">${row.lastName}</td>
                <td plain-value="${row.firstName}" class="firstName">${row.firstName}</td>
                <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                <td plain-value="${row.salary}" class="salary">${salaryFormattingEngine(row.salary)}</td>
                <td plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td plain-value="${row.deletedStatus}" class="deletedStatus">${row.deletedStatus}</td>
            </tr>`
    };

    GeneralMethods();
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "người",
            callModulesOfExtraFeatures: async () => {}
        }
    );
    await ListComponent(searchingSupportingDataSource);
    await CustomizeExportationFileModules();
})();
