class Dialog {
    constructor() {
    }

    async customizeToggleOpeningFormDialog(
        searchingSupportingDataSourceForDialog,
        addingFormDialogSupporterSelector='div.select-dialog'
    ) {
        const selectDialog = $(addingFormDialogSupporterSelector);

        //--Open dialog when clicking on icon in the add form
        const openDialogBtn = $(`div.center-page_adding-form div[id*="${searchingSupportingDataSourceForDialog.data.searchingField}"] i`);
        if (openDialogBtn != null)
            openDialogBtn.addEventListener("click", async e => {
                await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSourceForDialog)
                //--Open select-dialog.
                selectDialog.classList.remove("closed");
            });

        //--Auto fill input value when clicking on any row in select dialog
        $(addingFormDialogSupporterSelector + ` tbody`).addEventListener("click", e =>{
            $(`div.center-page_adding-form div[id*="${searchingSupportingDataSourceForDialog.data.searchingField}"] input`)
                .value = e.target.closest("tr").id.trim();
            selectDialog.classList.add("closed");
        });

        //--Customize closing dialog action.
        //--Stop propagation when clicking on dialog container so when select from list, it won't close the dialog
        selectDialog.querySelector('div.select-dialog-container').addEventListener("click", e => e.stopPropagation())
        //--Close dialog when clicking on dialog
        selectDialog.addEventListener("click", () => selectDialog.classList.add("closed"));
        //--Close dialog when clicking on close button
        selectDialog.querySelector('div.closing-dialog-btn').addEventListener("click", ()=> selectDialog.classList.add("closed"))

        CustomizeFetchingActionSpectator(
            searchingSupportingDataSourceForDialog,
            {callModulesOfExtraFeatures: () => {}},
            addingFormDialogSupporterSelector
        );
    }
}

class OrderDialog extends Dialog {
    constructor(tableBodySelector, roleFetching) {
        super();
        this.searchingSupportingDataSourceForDialog = {
            //--Initialize field-values for firstly fetch action.
            data: {
                currentPage: 1,
                objectsQuantity: 0,
                searchingField: "orderId",
                searchingValue: "",
            },

            //--Main fields for searching-action.
            tableBody: $(tableBodySelector),
            fetchDataAction: "/service/v1/" + roleFetching + "/find-order-for-supplies-importation-by-values",
            rowFormattingEngine: (row) => `
            <tr id="${row.orderId}">
                <td plain-value="${row.orderId}" class="orderId">${row.orderId}</td>
                <td plain-value="${row.employeeIdAsFk}" class="employeeIdAsFk">
                    ${row.employeeIdAsFk} - ${row.lastName + " " + row.firstName}
                </td>
                <td plain-value="${row.supplier}" class="supplier">${row.supplier}</td>
                <td plain-value="${row.warehouseIdAsFk}" class="warehouseIdAsFk">
                    ${row.warehouseIdAsFk} - ${row.warehouseName}
                </td>
                <td plain-value="${row.createdDate}" class="createdDate">${row.createdDate}</td>
            </tr>`
        };
    }

    async customizeToggleOpeningFormDialogDataSupporter(addingFormDialogSupporterSelector='div.select-dialog') {
        await super.customizeToggleOpeningFormDialog(
            this.searchingSupportingDataSourceForDialog,
            addingFormDialogSupporterSelector
        );
    }
}

class SupplyDialog extends Dialog {
    constructor(tableBodySelector, roleFetching) {
        super();
        this.searchingSupportingDataSourceForDialog = {
            //--Initialize field-values for firstly fetch action.
            data: {
                currentPage: 1,
                objectsQuantity: 0,
                searchingField: "supplyId",
                searchingValue: "",
            },

            //--Main fields for searching-action.
            tableBody: $(tableBodySelector),
            fetchDataAction: "/service/v1/" + roleFetching + "/find-supply-by-values",
            rowFormattingEngine: (row) => `
            <tr id="${row.supplyId}">
                <td plain-value="${row.supplyId}" class="supplyId">${row.supplyId}</td>
                <td plain-value="${row.supplyName}" class="supplyName">${row.supplyName}</td>
                <td plain-value="${row.unit}" class="unit">${row.unit}</td>
                <td plain-value="${row.quantityInStock}" class="quantityInStock">${row.quantityInStock}</td>
            </tr>`
        };
    }

    async customizeToggleOpeningFormDialogDataSupporter(addingFormDialogSupporterSelector='div.select-dialog') {
        await super.customizeToggleOpeningFormDialog(
            this.searchingSupportingDataSourceForDialog,
            addingFormDialogSupporterSelector
        );
    }
}

class WarehouseDialog extends Dialog {
    constructor(tableBodySelector, roleFetching) {
        super();
        this.searchingSupportingDataSourceForDialog = {
            //--Initialize field-values for firstly fetch action.
            data: {
                currentPage: 1,
                objectsQuantity: 0,
                searchingField: "warehouseId",
                searchingValue: "",
            },

            //--Main fields for searching-action.
            tableBody: $(tableBodySelector),
            fetchDataAction: "/service/v1/" + roleFetching + "/find-warehouse-by-values",
            rowFormattingEngine: (row) => `
            <tr id="${row.warehouseId}">
                <td plain-value="${row.warehouseId}" class="warehouseId">${row.warehouseId}</td>
                <td plain-value="${row.warehouseName}" class="warehouseName">${row.warehouseName}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
            </tr>`
        };
    }

    async customizeToggleOpeningFormDialogDataSupporter(addingFormDialogSupporterSelector='div.select-dialog') {
        await super.customizeToggleOpeningFormDialog(
            this.searchingSupportingDataSourceForDialog,
            addingFormDialogSupporterSelector
        );
    }
}