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
        tablePreviewTitle: 'Danh Sách Thông Tin Chi Tiết Vật Tư',
        fetchDataAction: `/service/v1/${roleForFetching}/find-supply-by-values`,
        usefulVariablesStorage: {},
        dataObject: {
            //--If page-number is "0", it's means that we will search all the list without pagination.
            currentPage: 0,
            searchingField: "supplyId",
            searchingValue: "",
            branch: "",
        },
        fieldObjects: [
            {cssName: "supplyId", utf8Name: "Mã Vật Tư"},
            {cssName: "supplyName", utf8Name: "Tên Vật Tư"},
            {cssName: "unit", utf8Name: "Đơn Vị Tính"},
            {cssName: "quantityInStock", utf8Name: "Số Lượng Tồn"},
        ],
        rowFormattingEngine: (row) => `
            <tr id="${row.supplyId}">
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.unit}" class="unit">${row.unit}</td>
                <td plain-value="${row.quantityInStock}" class="quantityInStock">${row.quantityInStock}</td>
            </tr>
            `,
        moreFeatuers: () => {},
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
                        $('.report-supporting-buttons_exporting-report').addEventListener("click", e =>
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
            searchingField: "supplyId",
            searchingValue: "",
            branch: "",
        },
        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-supply-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.supplyId}">
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.unit}" class="unit">${row.unit}</td>
                <td plain-value="${row.quantityInStock}" class="quantityInStock">${row.quantityInStock}</td>
            </tr>`
    };

    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "vật tư",
            callModulesOfExtraFeatures: async () => {}
        }
    );
    await ListComponent(searchingSupportingDataSource);
    await CustomizeExportationFileModules(roleForFetching);
})();
