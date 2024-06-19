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
        warehouseIdAsFk: {
            tag: $('input[name=warehouseIdAsFk]'),
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
    customizeSubmitFormAction('div.center-page_adding-form form', validatingBlocks);
    recoveryAllSelectTagData();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponentForSuppliesImportation(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div.center-page_list form', {mockTag: {isValid: true}})

    if (searchingSupportingDataSource.roleForFetching !== 'company')
        customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddSuppliesImportationComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-supplies-importation`,
        componentsForUpdating: [],
        moreActions: (updatedObjectRow) => {}
    };
    //--Searching data for suppliesImportation by suppliesImportationId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "suppliesImportationId",
            searchingValue: "",
            branch: $('div.table-tools .right-grid select[name=searchingBranch]').getAttribute("data").trim(),
        },

        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-supplies-importation-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.suppliesImportationId}" class="suppliesImportationId">${row.suppliesImportationId}</td>
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.employeeIdAsFk}" class="employeeIdAsFk">
                    ${row.employeeIdAsFk} - ${row.lastName + " " + row.firstName}
                </td>
                <td plain-value="${row.warehouseIdAsFk}" class="warehouseIdAsFk">
                    ${row.warehouseIdAsFk} - ${row.warehouseName}
                </td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td class="table-row-btn detail">
                    <a href="/${roleForFetching}/supplies-importation-detail/manage-supplies-importation-detail?suppliesImportationId=${row.suppliesImportationId}">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                </td>
                ${roleForFetching !== "company" ? `<td class="table-row-btn update">
                    <a id="${row.suppliesImportationId}"><i class="fa-regular fa-pen-to-square"></i></a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.suppliesImportationId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>` : ""}
            </tr>`
    };
    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "phiếu",
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
    await ListComponentForSuppliesImportation(searchingSupportingDataSource);
    if (roleForFetching !== 'company') {
        AddSuppliesImportationComponent();
        await CustomizeBuildingFormSpectator(
            async () => {
                await new OrderDialog(
                    'div#select-dialog_order table tbody',
                    roleForFetching,
                    null,
                    function moreActionCallback(trSelectedDom) {
                        $('.center-page_adding-form input[name=warehouseIdAsFk]').value
                            = trSelectedDom.querySelector('td.warehouseIdAsFk').textContent.split("-")[0].trim();
                    },
                ).customizeToggleOpeningFormDialogDataSupporter('div#select-dialog_order');
                await new WarehouseDialog(
                    'div#select-dialog_warehouse table tbody',
                    roleForFetching
                ).customizeToggleOpeningFormDialogDataSupporter('div#select-dialog_warehouse');
            },
            'div.center-page_adding-form'
        );
    }
})();