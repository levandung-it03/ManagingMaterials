(function main() {
    const validatingBlocks = {
        employeeId: {
            tag: $('input[name=employeeId]'),
            confirm: function (value) {
                this.isValid = (/^[0-9]$/).test(value);
                return this.isValid;
            },
            errorMessage: "Mã nhân viên không hợp lệ.",
            isValid: false,
        },
        identifier: {
            tag: $('input[name=identifier]'),
            confirm: function (value) {
                this.isValid = (/^[0-9]$/).test(value) && value.length >= 9;
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
                const isInThePast = (new Date(value) < new Date());
                const isNotTooFar = (new Date().getFullYear() - new Date(value).getFullYear()) < 150;
                this.isValid = isNotNaN && isInThePast && isNotTooFar
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
            errorMessage: "Lương không hợp lệ.",
            isValid: false,
        },
    };

    customizeAllAvatarColor();
    customizeClosingNoticeMessageEvent();
    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeSubmitFormAction(validatingBlocks);
    // recoveryAllSelectTagDataInForm();
    // mappingCategoryNameWithCurrentPage();
    customizeAutoFormatStrongInputTextEvent();
})();

function customizeAllAvatarColor() {
    [...$$('span.mock-avatar')].forEach(avatarTag => {
        const avatarColor = colorMap[avatarTag.innerText.trim().toUpperCase()];

        // Convert background color to RGB
        let r = parseInt(avatarColor.slice(1, 3), 16);
        let g = parseInt(avatarColor.slice(3, 5), 16);
        let b = parseInt(avatarColor.slice(5, 7), 16);

        // Calculate luminance
        let luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

        // Get the right letter's color
        const letterColor = (luminance > 0.5) ? "#000000" : "#FFFFFF";

        avatarTag.style.backgroundColor = avatarColor;
        avatarTag.style.color = letterColor;
    })
}