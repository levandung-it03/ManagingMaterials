function AddSuppliesImportationDetailComponent() {
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

async function ListComponentForSuppliesImportationDetail(searchingSupportingDataSource) {
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
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddSuppliesImportationDetailComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-supplies-importation-detail",
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
    //--Searching data for supplies importation detail by suppliesImportationId
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action
        data: {
            currentPage: 1,
            objectsQuantity: 1,
            searchingField: "supplyId",
            searchingValue: "",
            conditionObjectsList: [
                {
                    name: "suppliesImportationId",
                    value: $('input[name=suppliesImportationId]').value,
                }
            ]
        },

        //--Main fields for searching-action.
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-supplies-importation-detail-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.suppliesImportationId}">
                <td plain-value="${row.suppliesImportationId}" class="suppliesImportationId">${row.suppliesImportationId}</td>
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.suppliesQuantity}" class="suppliesQuantity">${row.suppliesQuantity}</td>
                <td plain-value="${row.price}" class="price">${salaryFormattingEngine(row.price)}</td>
                <td class="table-row-btn update">
                    <a id="${row.suppliesImportationId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.suppliesImportationId.trim()}">
                        <input name="supplyId" type="text" value="${row.supplyId.trim()}" hidden/>
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>
            </tr>`
    };

    GeneralMethods();
    AddSuppliesImportationDetailComponent();
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "phiếu",
            callModulesOfExtraFeatures: () => {
                //--Re-customize the listener of all updating-buttons.
                customizeGeneratingFormUpdateEvent('div.center-page_list', updatingSupportingDataSource);
            }
        }
    );
    await CustomizeBuildingFormSpectator(
        async () => {
            await new SupplyDialog(
                'div.select-dialog table tbody',
                "branch"
            ).customizeToggleOpeningFormDialogDataSupporter();
        },
        'div.center-page_adding-form'
    );
    await ListComponentForSuppliesImportationDetail(searchingSupportingDataSource);
})();