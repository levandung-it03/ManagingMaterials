(function AddEmployeeComponent() {
    const hiddenDataFields = {}
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

    function collectHiddenDataFields(hiddenDataFields) {
        [...$$('span.hidden-data-fields')].forEach(tag => {
            if (tag.getAttribute("type") === 'number') {
                hiddenDataFields[tag.getAttribute("name")] = Number.parseInt(tag.innerText);
            } else if (tag.getAttribute("type") === 'text') {
                hiddenDataFields[tag.getAttribute("name")] = tag.innerText;
            }
            tag.outerHTML = "";
        })
    }

    collectHiddenDataFields(hiddenDataFields);
    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div#center-page_adding-form form', validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    customizeAutoFormatStrongInputTextEvent();
})();

(function ListComponent() {
    const plainDataRows = $('table tbody').innerHTML;
    const rowFormattingEngine = (row) => `
        <tr id="${row.employeeId}">
            <td plain-value="${row.employeeId}" class="employee-id">${row.employeeId}</td>
            <td plain-value="${row.identifier} ${row.lastName} ${row.firstName}"
                class="base-profile">
                <span class="mock-avatar">${row.firstName.charAt(0)}</span>
                <div class="employee-info">
                    <b class="employee-name">${row.lastName} ${row.firstName}</b>
                    <p class="identifier">${row.identifier}</p>
                </div>
            </td>
            <td plain-value="${row.birthday}" class="birthday">${row.birthday}</td>
            <td plain-value="${row.salary}" class="address">${row.address}</td>
            <td plain-value="${row.salary}" class="salary">${row.salary}</td>
            <td class="table-row-btn update">
                <a href="/branch/employee/update-employee?employeeId=${row.employeeId}">
                    <i class="fa-regular fa-pen-to-square"></i>
                </a>
            </td>
            <td class="table-row-btn delete">
                <button name="deleteBtn" value="${row.employeeId}">
                    <i class="fa-regular fa-trash-can"></i>
                </button>
            </td>
        </tr>
    `;
    customizeSearchingListEvent(rowFormattingEngine, plainDataRows);
    customizeSortingListEvent();
    customizeSubmitFormAction('div#center-page_list form', { mockTag: { isValid: true } });
})();

(function GeneralMethods() {
    customizeAllAvatarColor();
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
})();