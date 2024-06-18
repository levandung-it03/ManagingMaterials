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
        username: {
            tag: $('input[name=username]'),
            validate: function (value) {
                //--Using function to make "this" works correctly.
                this.tag.value = value.trim().toUpperCase();
                return (/^[A-Za-z]+$/).test(value);
            },
            errorMessage: "Nhập đúng định dạng"
        },
        password: {
            tag: $('input[name=password]'),
            validate: function (value) {
                if (validatingBlocks.retypePassword.tag.value !== "")
                    validatingBlocks.retypePassword.tag.dispatchEvent(new Event("keyup"));
                return value.length >= 8;
            },
            errorMessage: "Mật khẩu không đủ dài."
        },
        retypePassword: {
            tag: $('input[name=retypePassword]'),
            validate: (value) => validatingBlocks.password.tag.value === value,
            errorMessage: "Mật khẩu không khớp."
        },
    };

    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction('div.center-page_adding-form form', validatingBlocks);
    customizeSubmitFormAction('div#form-dialog_adding-account form', validatingBlocks);
    recoveryAllSelectTagData();
    customizeAutoFormatStrongInputTextEvent();
}

async function ListComponent(searchingSupportingDataSource) {
    //--Firstly "fetch" data to put into empty-table-as-list.
    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);

    customizeSearchingListEvent(searchingSupportingDataSource);
    customizeRenderTableDataBySwitchingBranch(searchingSupportingDataSource);
    customizeSortingListEvent();

    customizeSubmitFormAction('div.center-page_list form', { mockTag: { isValid: true } });
}

async function customizeAddAccountFormDialog() {
    const formDialog = $('div#form-dialog');

    //--Customize closing form-dialog action.
    $('div#form-dialog_surrounding-frame').addEventListener("click", e => formDialog.classList.add("closed"));
    $('div.closing-dialog-btn').addEventListener("click", e => formDialog.classList.add("closed"));

    function customizeSelectingAddAccountBtn() {
        [...$$('div.center-page_list table tbody tr td.addAccount a i')].forEach(btn => {
            btn.addEventListener("click", async e => {
                const employeeId = e.target.parentElement.id;
                await fetch(
                    window.location.origin
                    + `/service/v1/branch/check-if-employee-account-is-existing?employeeId=${employeeId}`,
                    {//--Request Options
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({ })
                    }
                )
                    .then(response => {
                        if (response.ok)   return response.json();
                        else    throw new Error("Có lỗi xảy ra khi gửi yêu cầu.");
                    })
                    .then(responseObject => {
                        log(responseObject["isExistingEmployeeAccount"]);
                        if (responseObject["isExistingEmployeeAccount"]) {
                            $('div#message-block').innerHTML = `
                                <div class="error-service-message">
                                    <span>Nhân viên đã tồn tại tài khoản.</span>
                                    <i id="error-service-message_close-btn" class="fa fa-times-circle" aria-hidden="true"></i>
                                </div>`;
                            customizeClosingNoticeMessageEvent();
                        } else {
                            const dataRow = e.target.parentElement.parentElement.parentElement;

                            //--Clear form-dialog
                            [...formDialog.querySelectorAll('input')].forEach(inputTag => inputTag.value = "");
                            [...formDialog.querySelectorAll('.err-message-block')]
                                .forEach(errTag => errTag.style.display = "none");

                            //--Open form-dialog.
                            formDialog.classList.remove("closed");

                            //--Mapping data-row into form-dialog.
                            const mainFormInsideDialog = formDialog.querySelector('form');
                            mainFormInsideDialog.querySelector('input[name=employeeId]').value = employeeId;
                            mainFormInsideDialog.querySelector('input[name=fullName]').value =
                                dataRow.querySelector('b.lastName').getAttribute("plain-value")
                                +" "+dataRow.querySelector('b.firstName').getAttribute("plain-value");
                        }
                    });
            });
        });
    }

    customizeSelectingAddAccountBtn();
    customizeToggleDisplayPasswordEvent();
}

function GeneralMethods() {
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
}

(async function main() {
    const updatingSupportingDataSource = {
        addingFormCustomizer: AddEmployeeComponent,
        plainAddingForm: $('div.center-page div.center-page_adding-form form'),
        updatingAction: "/service/v1/branch/update-employee",
        componentsForUpdating: [
            //--Create 'select' block to serve selecting-another-branch.
            `<div class="form-select" id="branch">
                <fieldset>
                    <legend>Chi nhánh</legend>
                    <select data="" name="branch">${
                [...$$('div#branchesList span.hidden-data-fields')].map(tag => {
                    const value = tag.textContent.trim();
                    tag.outerHTML = null;
                    return `<option value="${value}">${value}</option>`;
                }).join("")
            }</select>
                </fieldset>
            </div>`
        ],
        moreActions: (updatedObjectRow) => {}
    };
    const searchingSupportingDataSource = {
        //--Initialize field-values for firstly fetch action.
        data: {
            currentPage: 1,
            objectsQuantity: 0,
            searchingField: "employeeId",
            searchingValue: "",
            branch: $('.table-tools .select-branch-to-search select').value,
        },

        //--Main fields for searching-action.
        tableBody: $('div.center-page_list table tbody'),
        fetchDataAction: "/service/v1/branch/find-employee-by-values",
        rowFormattingEngine: (row) => `
            <tr id="${row.employeeId}">
                <td plain-value="${row.employeeId}" class="employeeId">${row.employeeId}</td>
                <td class="base-profile">
                    <span class="mock-avatar">${row.firstName.charAt(0)}</span>
                    <div class="employee-info">
                        <span class="employeeName">
                            <b plain-value="${row.lastName}" class="lastName">${row.lastName}</b>
                            <b plain-value="${row.firstName}" class="firstName"> ${row.firstName}</b>
                        </span>
                        <p plain-value="${row.identifier}" class="identifier">${row.identifier}</p>
                    </div>
                </td>
                <td plain-value="${row.birthday.substring(0, 10)}" class="birthday">${row.birthday.substring(0, 10)}</td>
                <td plain-value="${row.address}" class="address">${row.address}</td>
                <td plain-value="${row.salary}" class="salary">${salaryFormattingEngine(row.salary)}</td>
                <td style="display:none" plain-value="${row.branch}" class="branch">${row.branch}</td>
                <td class="table-row-btn update">
                    <a id="${row.employeeId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </td>
                <td class="table-row-btn addAccount">
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

    GeneralMethods();
    AddEmployeeComponent();
    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            tableLabel: "người",
            callModulesOfExtraFeatures: async () => {
                //--Re-paint the colours of avatars.
                paintAllAvatarColor();
                //--Re-customize the listener of all adding-account-buttons.
                await customizeAddAccountFormDialog();
                //--Re-customize the listener of all updating-buttons.
                customizeGeneratingFormUpdateEvent(
                    'div.center-page_list',
                    updatingSupportingDataSource
                );
            }
        }
    );
    await ListComponent(searchingSupportingDataSource);
})();