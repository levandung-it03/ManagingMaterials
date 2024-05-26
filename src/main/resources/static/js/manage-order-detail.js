function AddOrderDetailComponent() {
    const validatingBlocks = {
        supplyId: {
            tag: $('input[name=supplyId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,3}\d{1,3}$/).test(this.tag.value)
            },
            errorMessage: "Mã vật tư không hợp lệ."
        },
        quantitySupply: {
            tag: $('input[name=quantitySupply]'),
            validate: (value) => value >= 0,
            errorMessage: "Số lượng phải >= 0."
        },
        price: {
            tag: $('input[name=price]'),
            validate: (value) => value >= 0,
            errorMessage: "Đơn giá phải >= 0"
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponentForOrderDetail(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div#center-page_list form', { mockTag: { isValid: true } });
}

function GeneralMethods() {
    // removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddOrderDetailComponent,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-order-detail",
        componentsForUpdating: []
    };
    //--Searching data for order detail by orderId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action
        data: {
            currentPage: 1,
            objectsQuantity: 1,
            searchingField: "supplyId",
            searchingValue: "",
            conditionObjectsList: [
                {
                    name: "orderId",
                    value: $('input[name=orderId]').value,
                }
            ]
        },

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-order-detail-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.quantitySupply}" class="quantitySupply">${row.quantitySupply}</td>
                <td plain-value="${row.price}" class="price">${salaryFormattingEngine(row.price)}</td>
                <td class="table-row-btn update">
                    <a id="${row.orderId} ${row.supplyId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.orderId} ${row.supplyId}">
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
            searchingField: "supplyId",
            searchingValue: "",
        },

        //--Main fields for searching-action.
        tableBody: $('div#select-dialog table tbody'),
        fetchDataAction: "/service/v1/branch/find-supply-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.supplyId}">
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.unit}" class="unit">${row.unit}</td>
                <td plain-value="${row.quantityInStock}" class="quantityInStock">${row.quantityInStock}</td>
            </tr>`
    };

    GeneralMethods();
    AddOrderDetailComponent();
    await CustomizeToggleOpeningAddingFormDialogSupporter(searchingSupportingDataSourceForDialog)
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "chi tiết đơn",
            callModulesOfExtraFeatures: () => {
                //--Re-customize the listener of all updating-buttons.
                customizeGeneratingFormUpdateEvent(
                    'div#center-page_list',
                    updatingSupportingDataSource
                );
            }
        }
    );
    await ListComponentForOrderDetail(searchingSupportingDataSource);
})();