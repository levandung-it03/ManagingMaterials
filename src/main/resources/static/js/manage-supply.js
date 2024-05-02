function AddSupplyComponent() {
    const validatingBlocks = {
        supplyId: {
            tag: $('input[name=supplyId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,2}\d{1,2}$/).test(this.tag.value)
            },
            errorMessage: "Mã vật tư không hợp lệ."
        },
        supplyName: {
            tag: $('input[name=supplyName]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.split(' ').map((word) => word.slice(0, 1).toUpperCase() + word.slice(1)).join(" ");
                return (/^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ0-9]{1,30})*$/).test(this.tag.value) && this.tag.value.length !== 0;
            },
            errorMessage: "Tên vật tư không hợp lệ."
        },
        unit: {
            tag: $('input[name=unit]'),
            validate: (value) => (/^[A-Za-zÀ-ỹ]{1,50}$/).test(value),
            errorMessage: "Đơn vị tính không được để trống."
        },
        quantityInStock: {
            tag: $('input[name=quantityInStock]'),
            validate: (value) => value >= 0,
            errorMessage: "Số lượng tồn phải >= 0."
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponent(AddSupplyComponentFunc) {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddSupplyComponentFunc,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-supply?supplyId=",
        componentsForUpdating: []
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        page: 1,
        objectsQuantity: 0,
        searchingField: "MAVT",
        searchingValue: "",

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-supply-by-values",
        objectsQuantityInTableCustomizer: () => $('#quantity').textContent = $$('table tbody tr').length + " vật tư",
        allAvatarColorCustomizer: () => {},
        rowFormattingEngine: (row) => `
            <tr id="${row.supplyId}">
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.unit}" class="unit">${row.unit}</td>
                <td plain-value="${row.quantityInStock}" class="quantityInStock">${row.quantityInStock}</td>
                <td class="table-row-btn update">
                    <a id="${row.supplyId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.supplyId}">
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
    AddSupplyComponent();
    await ListComponent(AddSupplyComponent);
    GeneralMethods();
})();