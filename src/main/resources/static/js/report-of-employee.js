
function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

async function customizeExportinsFileModules() {
    const pdfFilesExportator = new PdfFilesExportation();
    const fieldObjects = [
        {cssName: "employeeId", utf8Name: "Mã"},
        {cssName: "identifier", utf8Name: "CMND"},
        {cssName: "lastName", utf8Name: "Họ"},
        {cssName: "firstName", utf8Name: "Tên"},
        {cssName: "birthday", utf8Name: "Ngày sinh"},
        {cssName: "address", utf8Name: "Địa chỉ"},
        {cssName: "salary", utf8Name: "Lương"},
        {cssName: "branch", utf8Name: "Chi nhánh"},
        {cssName: "deletedStatus", utf8Name: "Đã xoá"},
    ];

    await pdfFilesExportator.loadAllNecessaryLibs()
        .then(() => {
            //--Build preview table data.
            pdfFilesExportator.buildPdfPages(fieldObjects)

            //--Customize clicking-preview-btn event.
            $('.report-supportting-buttons_preview').addEventListener("click", e =>
                $('div.preview-table-container').classList.remove("closed"));

            //--Customize clicking-pdf-exporting-btn event.
            $('.report-supportting-buttons_exporting-report').addEventListener("click", e =>
                pdfFilesExportator.exportToPdfFile());

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
                <td plain-value="${row.birthday}" class="birthday">${row.birthday}</td>
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
})

customizeExportinsFileModules();