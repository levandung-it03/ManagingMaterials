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

async function ListComponentForOrder(searchingSupportingDataSource) {
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
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddOrderComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-order`,
        componentsForUpdating: [],
        moreActions: (updatedObjectRow) => {}
    };
    //--Searching data for order by orderId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "orderId",
            searchingValue: "",
            branch: $('div.table-tools .right-grid select[name=searchingBranch]').getAttribute("data").trim(),
        },

        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-order-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId.trim()}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.employeeIdAsFk}" class="employeeIdAsFk">
                    ${row.employeeIdAsFk} - ${row.lastName + " " + row.firstName}
                </td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.warehouseIdAsFk}" class="warehouseIdAsFk">
                    ${row.warehouseIdAsFk} - ${row.warehouseName}
                </td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
                <td class="table-row-btn detail">
                    <a href="/${roleForFetching}/order-detail/manage-order-detail?orderId=${row.orderId}">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                </td>
                ${roleForFetching !== "company" ? `<td class="table-row-btn update">
                    <a id="${row.orderId}"><i class="fa-regular fa-pen-to-square"></i></a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.orderId}"><i class="fa-regular fa-trash-can"></i></button>
                </td>` : ""}
            </tr>`
    };

    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "đơn hàng",
            callModulesOfExtraFeatures: (roleForFetching) => {
                //--Re-customize the listener of all updating-buttons.
                if (roleForFetching !== "company")
                    customizeGeneratingFormUpdateEvent(
                        'div.center-page_list',
                        updatingSupportingDataSource
                    );
            }
        }
    );
    await ListComponentForOrder(searchingSupportingDataSource);
    if (roleForFetching !== "company") {
        AddOrderComponent();
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