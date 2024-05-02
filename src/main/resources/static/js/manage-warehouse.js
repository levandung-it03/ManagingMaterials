function AddEmployeeComponent() {
    const validatingBlocks = {
        warehouseId: {
            tag: $('input[name=warehouseId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,4}$/).test(this.tag.value)
            },
            errorMessage: "Mã kho không hợp lệ."
        },
        warehouseName: {
            tag: $('input[name=warehouseName]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.toUpperCase();
                return (/^[A-Za-zÀ-ỹ]{1,30}( [A-Za-zÀ-ỹ]{1,30})*$/).test(this.tag.value) && this.tag.value.length <= 30;
            },
            errorMessage: "Tên kho không hợp lệ."
        },
        address: {
            tag: $('input[name=address]'),
            validate: (value) => value.length !== 0,
            errorMessage: "Địa chỉ không được để trống."
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponent(AddWarehouseComponentFunc) {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddWarehouseComponentFunc,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-warehouse?warehouseId=",
        componentsForUpdating: []
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        page: 1,
        objectsQuantity: 0,
        searchingField: "MAKHO",
        searchingValue: "",

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-warehouse-by-values",
        objectsQuantityInTableCustomizer: () => $('#quantity').textContent = $$('table tbody tr').length + " kho",
        allAvatarColorCustomizer: () => {},
        rowFormattingEngine: (row) => `
            <tr id="${row.warehouseId}">
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.warehouseName}" class="warehouseName">${row.warehouseName}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                <td style="display:none" plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td class="table-row-btn update">
                    <a id="${row.warehouseId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.warehouseId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>
            </tr>`
    };
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchPaginatedDataByValues(searchingSupportingDataSource, updatingSupportingDataSource);
    customizePaginationBarAndFetchData(searchingSupportingDataSource, updatingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource, updatingSupportingDataSource);
    customizeUpdatingFormActionWhenUpdatingBtnIsClicked(updatingSupportingDataSource);

    customizeSubmitFormAction('div#center-page_list form', { mockTag: { isValid: true } });
    customizeSortingListEvent();
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    AddEmployeeComponent();
    await ListComponent(AddEmployeeComponent);
    GeneralMethods();
})();