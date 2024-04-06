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

const plainTableRows = [...$$('div#center-page_list table tbody tr')];
(function ListComponent() {
    customizeSearchingListEvent(plainTableRows);
    customizeSortingListEvent();
    customizeSubmitFormAction('div#center-page_list form', {mockTag: {isValid: true}});
})();

(function GeneralMethods() {
    customizeAllAvatarColor();
    removePathAttributes();
    customizeClosingNoticeMessageEvent();
})();