function AddOrderDetailComponent() {
    const validatingBlocks = {
        supplyId: {
            tag: $('input[name=supplyId]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Z]{1,4}\d{0,3}$/).test(this.tag.value) && value.length <= 4;
            },
            errorMessage: "Mã vật tư không hợp lệ."
        },
        suppliesQuantity: {
            tag: $('input[name=suppliesQuantity]'),
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
    customizeSubmitFormAction('div.center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagData();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponentForOrderDetail(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div.center-page_list form', { mockTag: { isValid: true } });
}

function GeneralMethods() {
    // removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const roleForFetching = getRoleFromJsp();
    const employeeIdLoggingIn = getEmployeeIdLoggingInFromJsp();
    const employeeIdFromHeader = $('.avatar .account-info span#employeeId').textContent.split(": ")[1].trim();
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddOrderDetailComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-order-detail`,
        componentsForUpdating: [],
        moreActions: (updatedObjectRow) => {
            (function customizeSupplyIdInputTagToServeUpdatingAction() {
                const supplyIdInpTagContainer = $('div.center-page_adding-form div#supplyId');
                supplyIdInpTagContainer.querySelector('i.fa-regular').outerHTML = null;
                supplyIdInpTagContainer.querySelector('input[name=supplyId]').readOnly = true;
                supplyIdInpTagContainer.querySelector('input[name=supplyId]').value =
                    updatedObjectRow.querySelector('.supplyId').textContent.trim();
            })();
        }
    };
    //--Searching data for order detail by orderId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action
        data: {
            currentPage: 1,
            objectsQuantity: 0,
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
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-order-detail-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                <td plain-value="${row.price}" class="price">${VNDCurrencyFormatEngine(row.price)}</td>
                ${(roleForFetching !== "company" && employeeIdLoggingIn == employeeIdFromHeader) ?
                `<td class="table-row-btn update">
                    <a id="${row.orderId} ${row.supplyId}"><i class="fa-regular fa-pen-to-square"></i></a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.orderId.trim()}-${row.supplyId.trim()}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>` : ""}
            </tr>`
    };

    GeneralMethods();
    CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "chi tiết đơn",
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
    await ListComponentForOrderDetail(searchingSupportingDataSource);
    if (roleForFetching !== "company") {
        AddOrderDetailComponent();
        await CustomizeBuildingFormSpectator(
            async () => {
                await new SupplyDialog(
                    'div.select-dialog table tbody',
                    roleForFetching
                ).customizeToggleOpeningFormDialogDataSupporter();
            },
            'div.center-page_adding-form'
        );
    }
})();