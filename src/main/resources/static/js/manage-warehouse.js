function AddWarehouseComponent() {
    const validatingBlocks = {
        warehouseId: {
            tag: $('input[name=warehouseId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,4}\d{0,3}$/).test(this.tag.value) && value.length <= 4;
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
    customizeSubmitFormAction('div.center-page_adding-form form', validatingBlocks);
    recoveryAllSelectTagData();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div.center-page_list form', { mockTag: { isValid: true } });
    if (searchingSupportingDataSource.roleForFetching === "company")
        customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddWarehouseComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-warehouse`,
        componentsForUpdating: [],
        moreActions: (updatedObjectRow) => {}
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "warehouseId",
            searchingValue: "",
            branch: $('div.table-tools .right-grid select[name=searchingBranch]').getAttribute("data").trim(),
        },

        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-warehouse-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.warehouseId}">
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.warehouseName}" class="warehouseName">${row.warehouseName}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                ${roleForFetching !== "company" ? `<td class="table-row-btn update">
                    <a id="${row.warehouseId}"><i class="fa-regular fa-pen-to-square"></i></a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.warehouseId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>` : ""}
            </tr>`
    };
    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "kho",
            callModulesOfExtraFeatures: (roleForFetching) => {
                //--Re-customize the listener of all updating-buttons.
                if (roleForFetching !== 'company')
                    customizeGeneratingFormUpdateEvent(
                        'div.center-page_list',
                        updatingSupportingDataSource
                    );
            }
        }
    );
    await ListComponent(searchingSupportingDataSource);
    if (roleForFetching !== 'company')  AddWarehouseComponent();
})();