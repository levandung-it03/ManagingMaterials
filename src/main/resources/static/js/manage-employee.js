function AddEmployeeComponent() {
    const validatingBlocks = {
        identifier: {
            tag: $('input[name=identifier]'),
            validate: (value) => (/^[0-9]{9,20}$/).test(value),
            errorMessage: "CMND không hợp lệ."
        },
        lastName: {
            tag: $('input[name=lastName]'),
            validate: (value) => (/^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$/).test(value),
            errorMessage: "Họ nhân viên không hợp lệ."
        },
        firstName: {
            tag: $('input[name=firstName]'),
            validate: (value) => (/^[A-Za-zÀ-ỹ]{1,50}$/).test(value),
            errorMessage: "Tên nhân viên không hợp lệ."
        },
        birthday: {
            tag: $('input[name=birthday]'),
            validate: (value) => (!isNaN(new Date(value))   //--is not "NaN".
                && (new Date().getFullYear() - new Date(value).getFullYear()) >= 18     //--is adults
                && (new Date().getFullYear() - new Date(value).getFullYear()) < 150),   //--is not too old
            errorMessage: "Ngày sinh không hợp lệ."
        },
        address: {
            tag: $('input[name=address]'),
            validate: (value) => value.length !== 0,
            errorMessage: "Địa chỉ không được để trống."
        },
        salary: {
            tag: $('input[name=salary]'),
            validate: (value) => value >= 4000000,
            errorMessage: "Lương phải >= 4.000.000"
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponent(AddEmployeeComponentFunc) {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddEmployeeComponentFunc,
        plainAddingForm: $('div#center-page div#center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-employee?employeeId=",
        componentsForUpdating: [
            //--Create 'select' block to serve selecting-another-branch.
            `<div class="form-select" id="branch">
                <fieldset>
                    <legend>Chi nhánh</legend>
                    <select data="" name="branch">${
                        [...$$('div#branchesList span.hidden-data-fields')].map(tag => {
                            const value = tag.textContent.trim();
                            return `<option value="${value}">${value}</option>`;
                        }).join("")
                    }</select>
                </fieldset>
            </div>`
        ]
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        currentPage: 1,
        objectsQuantity: 0,
        searchingField: "MANV",
        searchingValue: "",

        //--Main fields for searching-action.
        tableBody: $('div#center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-employee-by-values",
        objectsQuantityInTableCustomizer: () => $('#quantity').textContent = $$('table tbody tr').length + " người",
        allAvatarColorCustomizer: customizeAllAvatarColor,
        rowFormattingEngine: (row) => `
            <tr id="${row.employeeId}">
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td plain-value="${row.identifier} ${row.lastName} ${row.firstName}" class="base-profile">
                    <span class="mock-avatar">${row.firstName.charAt(0)}</span>
                    <div class="employee-info">
                        <span class="employeeName">
                            <b class="lastName">${row.lastName}</b>
                            <b class="firstName"> ${row.firstName}</b>
                        </span>
                        <p class="identifier">${row.identifier}</p>
                    </div>
                </td>
                <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
                <td plain-value="${row.salary}" class="address">${row.address}</td>
                <td plain-value="${row.salary}" class="salary">${row.salary}</td>
                <td style="display:none" plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td class="table-row-btn update">
                    <a id="${row.employeeId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn delete">
                    <button name="deleteBtn" value="${row.employeeId}">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </td>
            </tr>`
    };
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchPaginatedDataByValues(searchingSupportingDataSource, updatingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource, updatingSupportingDataSource);
    customizeUpdatingFormActionWhenUpdatingBtnIsClicked(updatingSupportingDataSource);

    customizeSortingListEvent();
    customizeSubmitFormAction('div#center-page_list form', { mockTag: { isValid: true } });
}

function GeneralMethods() {
    customizeAllAvatarColor();
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    AddEmployeeComponent();
    await ListComponent(AddEmployeeComponent);
    GeneralMethods();
})();