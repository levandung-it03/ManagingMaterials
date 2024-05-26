function AddOrderComponent() {
    const validatingBlocks = {
        orderId: {
            tag: $('input[name=orderId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,7}\d{1,7}$/).test(this.tag.value) && value.length <= 8;
            },
            errorMessage: "Mã đơn đặt hàng không hợp lệ."
        },
        supplier: {
            tag: $('input[name=supplier]'),
            validate: (value) => value.length !== 0,
            errorMessage: "Tên nhà cung cấp không được trống."
        },
        createdDate: {
            tag: $('input[name=createdDate]'),
            validate: (value) => (!isNaN(new Date(value)) && new Date(value) >= new Date()),
            errorMessage: "Ngày tạo phải >= ngày hiện tại."
        },
        warehouseId: {
            tag: $('input[name=warehouseId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,3}\d{1,3}$/).test(this.tag.value) && value.length <= 4;
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

async function ListComponentForOrder(searchingSupportingDataSource) {
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
        addingFormCustomizer: AddOrderComponent,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-order",
        componentsForUpdating: []
    };
    //--Searching data for order by orderId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 1,
            searchingField: "orderId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-order-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td class="table-row-btn detail">
                    <a href="/branch/order-detail/manage-order-detail?orderId=${row.orderId}">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                </td>
                <td class="table-row-btn update">
                    <a id="${row.orderId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.orderId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>
            </tr>`
    };
    const searchingSupportingDataSourceForDialog = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "warehouseId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#select-dialog table tbody'),
        fetchDataAction: "/service/v1/branch/find-warehouse-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.warehouseId}">
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.warehouseName}" class="warehouseName">${row.warehouseName}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
            </tr>`
    };

    GeneralMethods();
    AddOrderComponent();

    await CustomizeToggleOpeningAddingFormDialogSupporter(searchingSupportingDataSourceForDialog)
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "đơn hàng",
            callModulesOfExtraFeatures: () => {
                //--Re-customize the listener of all updating-buttons.
                customizeGeneratingFormUpdateEvent(
                    'div#center-page_list',
                    updatingSupportingDataSource
                );
            }
        }
    );
    await ListComponentForOrder(searchingSupportingDataSource);
})();