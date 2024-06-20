
async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent('div.center-page_list table');
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
        tablePreviewTitle: 'Danh sách đơn đặt hàng chưa có phiếu nhập',
        fetchDataAction: `/service/v1/${roleForFetching}/find-order-dont-have-import-for-report`,
        usefulVariablesStorage: {},
        dataObject: {
            //--If page-number is "0", it's means that we will search all the list without pagination.
            currentPage: 0,
            searchingField: "orderId",
            searchingValue: "",
            branch: "",
        },
        fieldObjects: [
            {cssName: "orderId", utf8Name: "Mã"},
            {cssName: "createdDate", utf8Name: "Ngày tạo"},
            {cssName: "supplier", utf8Name: "Nhà cung cấp"},
            {cssName: "employeeFullName", utf8Name: "Họ tên nhân viên"},
            {cssName: "supplyName", utf8Name: "Tên vật tư"},
            {cssName: "suppliesQuantity", utf8Name: "Số lượng"},
            {cssName: "price", utf8Name: "Đơn giá"},
        ],
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.employeeFullName}" class="employeeFullName">${row.employeeFullName}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                <td plain-value="${row.price}" class="price">${row.price}</td>
            </tr>`,
        moreFeatures: async () =>  {
            const result = await pdfFilesExporter.fetchDataForReporter(fetchingConfigObject)
            const tbody = document.createElement("tbody")
            tbody.innerHTML = result
            const rows = tbody.querySelectorAll('tr');
            let previousOrderId = '';
            let orderIdCell, createdDateCell, supplierCell, employeeFullNameCell;
            rows.forEach((row, index) => {
                const cells = row.querySelectorAll('td')
                const orderId = cells[0].getAttribute('plain-value');
                if (orderId === previousOrderId) {
                    // If the orderId is the same as the previous row, merge cells
                    cells.forEach((cell,index) => {
                        cell.style.borderTop = '0'
                        if (index < 4) cell.textContent = ''
                    })
                } else {
                    // If the orderId is different, update the previous cells
                    orderIdCell = cells[0]
                    createdDateCell = cells[1]
                    supplierCell = cells[2]
                    employeeFullNameCell = cells[3]
                    previousOrderId = orderId;
                }
            });
            const exportTable = $('.exporting-table-css tbody')
            exportTable.innerHTML = tbody.innerHTML
        },
    };

    await pdfFilesExporter.loadAllNecessaryLibs()
        .then(() => {
            //--Customize clicking-preview-btn event.
            $('.report-supporting-buttons_preview').addEventListener("click", async e => {
                //--Prepare data for preview-descriptions.
                fetchingConfigObject.descriptionComponents = [];
                //--Prepare data for preview-statistic.
                fetchingConfigObject.statisticComponents = [];

                //--Build preview table data.
                await pdfFilesExporter.buildPreviewPages(fetchingConfigObject)
                    .then(() => {
                        //--Open preview-page after building page successfully.
                        $(previewInfoContainer).style.minHeight = $('html body').offsetHeight + "px";
                        $(previewInfoContainer).classList.remove("closed");

                        //--Customize clicking-pdf-exporting-btn event.
                        $('.report-supporting-buttons_exporting-report').addEventListener("click", () =>
                            pdfFilesExporter.exportToPdfFile(previewInfoContainer + ' table'));

                    })
                    .catch(err => console.log("Error when building-preview-page: " + err));
            });
        })
        .catch(err => {
            alert("Có lỗi xảy ra khi xây dựng 'Bảng thông tin xem trước', vui lòng đợi thêm vài giây để tài nguyên kịp thời tải.");
        });
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "orderId",
            searchingValue: "",
            branch: "",
        },
        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-order-dont-have-import-for-report`,
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.employeeFullName}" class="employeeFullName">${row.employeeFullName}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                <td plain-value="${row.price}" class="price">${row.price}</td>
            </tr>`,
    };

    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "đơn",
            callModulesOfExtraFeatures: async () => {
            }
        }
    );
    await ListComponent(searchingSupportingDataSource);
    await CustomizeExportationFileModules(roleForFetching);
})();
