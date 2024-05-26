function AddSuppliesImportationComponent() {
    const validatingBlocks = {
        suppliesImportationId: {
            tag: $('input[name=suppliesImportationId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,7}\d{1,7}$/).test(this.tag.value) && value.length <= 8;
            },
            errorMessage: "Mã phiếu nhập không hợp lệ."
        },
        orderId: {
            tag: $('input[name=orderId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,7}\d{1,7}$/).test(this.tag.value) && value.length <= 8;
            },
            errorMessage: "Mã đơn đặt hàng không hợp lệ."
        },
        warehouseId: {
            tag: $('input[name=warehouseId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,4}\d{0,3}$/).test(this.tag.value) && value.length <= 4;
            },
            errorMessage: "Mã kho không hợp lệ."
        }
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponentForSuppliesImportation(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div#center-page_list form', {mockTag: {isValid: true}});
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddSuppliesImportationComponent,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-supplies-importation",
        componentsForUpdating: []
    };
    //--Searching data for order by orderId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 1,
            searchingField: "suppliesImportationId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-supplies-importation-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.suppliesImportationId}" class="suppliesImportationId">${row.suppliesImportationId}</td>
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td class="table-row-btn detail">
                    <a href="/branch/order-detail/manage-order-detail?orderId=${row.suppliesImportationId}">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                </td>
                <td class="table-row-btn update">
                    <a id="${row.suppliesImportationId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.suppliesImportationId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>
            </tr>`
    };
    const searchingSupportingDataSourceForOrderDialog = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "orderId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#select-dialog_order table tbody'),
        fetchDataAction: "/service/v1/branch/find-order-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.employeeId}" class="employeeId">
                    ${row.employeeId} - ${row.lastName + " " + row.firstName}
                </td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.warehouseId}" class="warehouseId">
                    ${row.warehouseId} - ${row.warehouseName}
                </td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
            </tr>`
    };
    const searchingSupportingDataSourceForWarehouseDialog = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "warehouseId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#select-dialog_warehouse table tbody'),
        fetchDataAction: "/service/v1/branch/find-warehouse-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.warehouseId}">
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.warehouseName}" class="warehouseName">${row.warehouseName}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
            </tr>`
    };

    GeneralMethods();
    AddSuppliesImportationComponent();

    await CustomizeToggleOpeningAddingFormDialogSupporter(
        searchingSupportingDataSourceForOrderDialog,
        'div#select-dialog_order'
    )
    await CustomizeToggleOpeningAddingFormDialogSupporter(
        searchingSupportingDataSourceForWarehouseDialog,
        'div#select-dialog_warehouse'
    )
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "phiếu",
            callModulesOfExtraFeatures: () => {
                //--Re-customize the listener of all updating-buttons.
                customizeGeneratingFormUpdateEvent(
                    'div#center-page_list',
                    updatingSupportingDataSource
                );
            }
        }
    );
    await ListComponentForSuppliesImportation(searchingSupportingDataSource);
})();