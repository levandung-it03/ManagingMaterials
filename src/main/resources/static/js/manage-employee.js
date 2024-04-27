(function AddEmployeeComponent() {
    const validatingBlocks = {
        identifier: {
            tag: $('input[name=identifier]'),
            confirm: function (value) {
                this.isValid = (/^[0-9]{9,20}$/).test(value);
                return this.isValid;
            },
            errorMessage: "CMND không hợp lệ.",
            isValid: false,
        },
        lastName: {
            tag: $('input[name=lastName]'),
            confirm: function (value) {
                this.isValid = (/^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$/).test(value);
                return this.isValid;
            },
            errorMessage: "Họ nhân viên không hợp lệ.",
            isValid: false,
        },
        firstName: {
            tag: $('input[name=firstName]'),
            confirm: function (value) {
                this.isValid = (/^[A-Za-zÀ-ỹ]{1,50}$/).test(value);
                return this.isValid;
            },
            errorMessage: "Tên nhân viên không hợp lệ.",
            isValid: false,
        },
        birthday: {
            tag: $('input[name=birthday]'),
            confirm: function (value) {
                const isNotNaN = !isNaN(new Date(value));
                const isAdults = (new Date().getFullYear() - new Date(value).getFullYear()) >= 18;
                const isNotTooOld = (new Date().getFullYear() - new Date(value).getFullYear()) < 150;
                this.isValid = isNotNaN && isAdults && isNotTooOld
                return this.isValid;
            },
            errorMessage: "Ngày sinh không hợp lệ.",
            isValid: false,
        },
        address: {
            tag: $('input[name=address]'),
            confirm: function (value) {
                this.isValid = value.length !== 0;
                return this.isValid;
            },
            errorMessage: "Địa chỉ không được để trống.",
            isValid: false,
        },
        salary: {
            tag: $('input[name=salary]'),
            confirm: function (value) {
                this.isValid = value >= 4000000;
                return this.isValid;
            },
            errorMessage: "Lương phải >= 4.000.000",
            isValid: false,
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
})();

(function ListComponent() {
    const updatingSupportingDataSource = {
        plainAddingForm: $('div#center-page div#center-page_adding-form form').cloneNode(true),
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
        plainDataRows: $('table tbody').innerHTML,
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

    customizeSearchingListEvent(searchingSupportingDataSource, updatingSupportingDataSource);
    customizeSortingListEvent();
    customizeSubmitFormAction('div#center-page_list form', { mockTag: { isValid: true } });
    customizeUpdatingFormActionWhenUpdatingBtnIsClicked(updatingSupportingDataSource);
})();

(function GeneralMethods() {
    customizeAllAvatarColor();
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
})();