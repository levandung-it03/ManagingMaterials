(function main() {
    const validatingBlocks = {
        username: {
            tag: $('input[name=username]'),
            confirm: function (value) {
                this.tag.value = value.trim().toUpperCase();
                this.isValid = (/^[A-Z]+$/).test(value);
                return this.isValid;
            },
            errorMessage: "Nhập đúng định dạng",
            isValid: false,
        },
        password: {
            tag: $('input[name=password]'),
            confirm: function (value) {
                this.isValid = value.length >= 8;
                return this.isValid;
            },
            errorMessage: "Mật khẩu không đủ dài.",
            isValid: false,
        },
    };

    customizeClosingNoticeMessageEvent();
    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeToggleDisplayPasswordEvent();
    customizeSubmitFormAction(validatingBlocks);
    removePathAttributes();
})();
