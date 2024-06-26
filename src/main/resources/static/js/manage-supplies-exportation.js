function AddSuppliesExportationComponent() {
    const validatingBlocks = {
        suppliesExportationId: {
            tag: $('input[name=suppliesExportationId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,7}\d{1,7}$/).test(this.tag.value) && value.length <= 8;
            },
            errorMessage: "Mã phiếu xuất không hợp lệ."
        },
        customerFullName: {
            tag: $('input[name=customerFullName]'),
            validate: (value) => (/^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$/).test(value),
            errorMessage: "Tên khách hàng không hợp lệ."
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

async function ListComponentForSuppliesExportation(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div.center-page_list form', {mockTag: {isValid: true}});
    if (searchingSupportingDataSource.roleForFetching === "company")
        customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    const employeeIdLoggingIn = getEmployeeIdLoggingInFromJsp();
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddSuppliesExportationComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-supplies-exportation`,
        componentsForUpdating: [],
        moreActions: (updatedObjectRow) => {}
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "suppliesExportationId",
            searchingValue: "",
            branch: $('div.table-tools .right-grid select[name=searchingBranch]').getAttribute("data").trim(),
        },

        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-supplies-exportation-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.suppliesExportationId.trim()}">
                <td plain-value="${row.suppliesExportationId}" class="suppliesExportationId">${row.suppliesExportationId}</td>
                <td plain-value="${row.employeeIdAsFk}" class="employeeIdAsFk">
                    ${row.employeeIdAsFk} - ${row.lastName + " " + row.firstName}
                </td>
                <td plain-value="${row.customerFullName}" class="customerFullName">${row.customerFullName}</td>
                <td plain-value="${row.warehouseIdAsFk}" class="warehouseIdAsFk">
                    ${row.warehouseIdAsFk} - ${row.warehouseName}
                </td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td class="table-row-btn detail">
                    <a href="/${roleForFetching}/supplies-exportation-detail/manage-supplies-exportation-detail?suppliesExportationId=${row.suppliesExportationId}">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                </td>
                ${(roleForFetching !== "company" && employeeIdLoggingIn == row.employeeIdAsFk) ?
                `<td class="table-row-btn update">
                    <a id="${row.suppliesExportationId}"><i class="fa-regular fa-pen-to-square"></i></a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.suppliesExportationId}">
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
                if (roleForFetching !== 'company') {
                    customizeGeneratingFormUpdateEvent(
                        'div.center-page_list',
                        updatingSupportingDataSource
                    );
                }
            }
        }
    );
    await ListComponentForSuppliesExportation(searchingSupportingDataSource);
    if (roleForFetching !== 'company') {
        AddSuppliesExportationComponent();
        await CustomizeBuildingFormSpectator(
            async () => {
                await new WarehouseDialog(
                    'div.select-dialog table tbody',
                    roleForFetching
                ).customizeToggleOpeningFormDialogDataSupporter();
            },
            'div.center-page_adding-form'
        );
    }
})();