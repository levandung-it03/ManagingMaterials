(function main() {
    const validatingBlocks = {
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
            validate: (value) => value.length >= 8,
            errorMessage: "Mật khẩu không đủ dài."
        },
    };

    customizeClosingNoticeMessageEvent();
    createErrBlocksOfInputTags(validatingBlocks);
    customizeValidateEventInputTags(validatingBlocks);
    customizeToggleDisplayPasswordEvent();
    customizeSubmitFormAction('form', validatingBlocks);
    removePathAttributes();
})();
