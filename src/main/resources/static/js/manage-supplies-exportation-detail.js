function AddSuppliesExportationDetailComponent() {
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
            validate: (value) => {
                //--For adding
                if ($('input[type=submit]').value.trim().toUpperCase() !== "CẬP NHẬT")
                    return (value > 0)
                        && (value <= Number.parseInt($('.center-page_adding-form input[name=quantityInStock]').value.trim()));
                return true;
            },
            errorMessage: "Số lượng không quá số lượng đặt."
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

async function ListComponentForSuppliesExportationDetail(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeSortingListEvent();
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
        addingFormCustomizer: AddSuppliesExportationDetailComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: `/service/v1/${roleForFetching}/update-supplies-exportation-detail`,
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
    //--Searching data for supplies exportation detail by suppliesExportationId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "supplyId",
            searchingValue: "",
            conditionObjectsList: [
                {
                    name: "suppliesExportationId",
                    value: $('input[name=suppliesExportationId]').value,
                }
            ]
        },

        //--Main fields for searching-action.
        roleForFetching: roleForFetching,
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: `/service/v1/${roleForFetching}/find-supplies-exportation-detail-by-values`,
        rowFormattingEngine: (row) => `
            <tr id="${row.suppliesExportationId}">
                <td plain-value="${row.suppliesExportationId}" class="suppliesExportationId">${row.suppliesExportationId}</td>
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                <td plain-value="${row.price}" class="price">${VNDCurrencyFormatEngine(row.price)}</td>
                ${(roleForFetching !== "company" && employeeIdLoggingIn == employeeIdFromHeader) ?
                `<td class="table-row-btn update">
                    <a id="${row.suppliesExportationId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
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
                if (roleForFetching !== "company")
                    customizeGeneratingFormUpdateEvent(
                        'div.center-page_list',
                        updatingSupportingDataSource
                    );
            }
        }
    );
    await ListComponentForSuppliesExportationDetail(searchingSupportingDataSource);
    if (roleForFetching !== "company") {
        AddSuppliesExportationDetailComponent();
        await CustomizeBuildingFormSpectator(
            async () => {
                await new SupplyDialog(
                    'div.select-dialog table tbody',
                    roleForFetching,
                    null,
                    function moreActionCallback(trSelectedDom) {
                        $('.center-page_adding-form input[name=suppliesQuantity]').value
                            = $('.center-page_adding-form input[name=quantityInStock]').value
                            = trSelectedDom.querySelector('td.quantityInStock').textContent.trim();
                    }
                ).customizeToggleOpeningFormDialogDataSupporter();
            },
            'div.center-page_adding-form'
        );
    }
})();