const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const urlParams = new URLSearchParams(window.location.search);
const colorMap = {
    A: "#FFBF00",
    B: "#0000FF",
    C: "#00FFFF",
    D: "#1560BD",
    E: "#50C878",
    F: "#FF00FF",
    G: "#FFD700",
    H: "#DF73FF",
    I: "#4B0082",
    J: "#00A86B",
    K: "#F0E68C",
    L: "#E6E6FA",
    M: "#FF00FF",
    N: "#000080",
    O: "#808000",
    P: "#FFC0CB",
    Q: "#51484F",
    R: "#FF0000",
    S: "#C0C0C0",
    T: "#40E0D0",
    U: "#3F00FF",
    V: "#8A2BE2",
    W: "#FFFFFF",
    X: "#738678",
    Y: "#FFFF00",
    Z: "#0014A8"
};

function customizeClosingNoticeMessageEvent() {
    const errMessageCloseBtn = $('div#message-block div.error-service-message i#error-service-message_close-btn');
    const succeedMessageCloseBtn = $('div#message-block div.succeed-service-message i#succeed-service-message_close-btn');

    if (errMessageCloseBtn != null) {
        setTimeout(() => $('div#message-block div.error-service-message').classList.add("hide"), 6000);
        errMessageCloseBtn.addEventListener("click", (e) => {
            $('div#message-block div.error-service-message').classList.add("hide");
        });
    }
    if (succeedMessageCloseBtn != null) {
        setTimeout(() => $('div#message-block div.succeed-service-message').classList.add("hide"), 6000);
        succeedMessageCloseBtn.addEventListener("click", (e) => {
            $('div#message-block div.succeed-service-message').classList.add("hide");
        });
    }

}

function createErrBlocksOfInputTags(validatingBlocks) {
    [...$$('.form-input .form_text-input_err-message')].forEach((e) => {
        e.innerHTML = `
        <span class='err-message-block' id='${e.parentNode.id}'>
            ${validatingBlocks[e.parentNode.id].errorMessage}
        </span>`;
    })
}

function customizeValidateEventInputTags(validatingBlocks) {
    Object.entries(validatingBlocks).forEach(elem => {
        const toggleShowMessage = (elem) => {
            if (elem[1].validate(elem[1].tag.value)) $('span#' + elem[0]).style.display = "none";
            else    $('span#' + elem[0]).style.display = "inline";
        };
        elem[1].tag.addEventListener("keyup", e => toggleShowMessage(elem));
        elem[1].tag.addEventListener("change", e => toggleShowMessage(elem));
    });
}

function customizeSubmitFormAction(formSelector, validatingBlocks) {
    $(formSelector).onsubmit = e => {
        console.log(validatingBlocks);
        if (confirm("Bạn chắc chắn muốn thực hiện thao tác?") === true) {
            let isValid = Object.entries(validatingBlocks)
                .every(elem => elem[1].validate(elem[1].tag.value));
            if (!isValid) alert("Thông tin đầu vào bị lỗi!");
            return isValid;
        } else return false;
    }
}

function removePathAttributes() {
    let newUrl = `${window.location.pathname}`;
    if (urlParams.has("page"))
        newUrl += `?page=${urlParams.get("page")}`;

    history.replaceState(null, '', newUrl);
}

function customizeToggleDisplayPasswordEvent() {
    $$('.password_toggle-hidden i').forEach((eye) => {
        eye.onclick = (e) => {
            if ([...e.target.classList].some((e) => e === "show-pass")) {
                e.target.classList.add("hidden");
                e.target.parentElement.querySelector(".hide-pass").classList.remove("hidden");
                $(`input[name=${e.target.id}]`).type = "text";
            } else {
                e.target.classList.add("hidden");
                e.target.parentElement.querySelector(".show-pass").classList.remove("hidden");
                $(`input[name=${e.target.id}]`).type = "password";
            }
        }
    })
}

function cuttingStringValueOfInputTag(tag, len) {
    if (tag.value.length > len)
        tag.value = tag.value.slice(0, len);
}

function recoveryAllSelectTagDataInForm() {
    [...$$('form select')].forEach(selectTag => {
        const data = selectTag.getAttribute('data');
        if (data != null) {
            [...selectTag.querySelectorAll('option')].forEach(optionTag => {
                if (optionTag.value === data)
                    optionTag.selected = "true";
            });
        }
    });
}

function customizeSearchingListEvent(searchingSupportingDataSource, updatingSupportingDataSource) {
    const searchingInputTag = $('#table-search-box input#search');
    const selectedOption = $('#table-search-box select#search');

    const handleSearchingListEvent = async e => {
        const tableBody = $('table tbody');

        //--Stop searching if searching-input-value is empty.
        if (searchingInputTag.value === "")     tableBody.innerHTML = searchingSupportingDataSource.plainDataRows;

        //--Stop searching if searched-field is not selected yet.
        else if (selectedOption.value === "")   alert("Bạn hãy chọn trường cần tìm kiếm trước!");

        //--Start data with selected field by calling an API.
        else {
            //--Use await to make this "fetch" action sync with this "handleSearchingListEvent" method.
            await fetch(
                window.location.origin + "/service/v1/branch/find-employee-by-values",
                {//--Request Options
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        searchingField: selectedOption.value,
                        searchingValue: searchingInputTag.value
                    })
                }
            )
                .then(response => {
                    if (response.ok)   return response.json();
                    else    throw new Error("Có lỗi xảy ra khi gửi yêu cầu.");
                })
                .then(foundDataSet => {
                    if (foundDataSet.length === 0) {
                        tableBody.innerHTML = '<tr><td style="width: 100%">Không tìm thấy dữ liệu vừa nhập</td></tr>';
                    } else {
                        tableBody.innerHTML = foundDataSet
                            .map(dataOfRow => searchingSupportingDataSource.rowFormattingEngine(dataOfRow))
                            .join("");
                    }
                })
                .catch(error => console.error("Đã có lỗi xảy ra:", error));
        }
        $('#quantity').textContent = $$('table tbody tr').length;
        customizeAllAvatarColor();
        customizeUpdatingFormActionWhenUpdatingBtnIsClicked(updatingSupportingDataSource);
        return null;
    }

    $('#table-search-box i').addEventListener("click", handleSearchingListEvent);
    searchingInputTag.addEventListener("keyup", handleSearchingListEvent);
}

function customizeSortingListEvent() {
    [...$$('table thead th i')].forEach(btn => {
        btn.addEventListener("click", e => {
            const fieldId = e.target.parentElement.id;
            const cellOfFields = [...$$('table tbody td.' + fieldId)];
            const firstCellOfSearchingColumn = cellOfFields[0].getAttribute('plain-value');
            let searchingDataFieldType = null;

            if (Number.parseInt(firstCellOfSearchingColumn) !== null)   searchingDataFieldType = "Number";
            else if (new Date(firstCellOfSearchingColumn) !== null)     searchingDataFieldType = "Date";
            else    searchingDataFieldType = "String";

            cellOfFields.sort((a, b) => {
                const firstCell = a.getAttribute('plain-value');
                const secondCell = b.getAttribute('plain-value');

                if (searchingDataFieldType === "Number") return Number.parseInt(firstCell) - Number.parseInt(secondCell);
                else if (searchingDataFieldType === "Date")   return new Date(firstCell) < new Date(secondCell);
                else    return firstCell.localeCompare(secondCell);
            });
            alert("Sắp xếp thành công!");
            $('table tbody').innerHTML = cellOfFields.reduce((accumulator, cell) => {
                return accumulator + cell.parentElement.outerHTML;
            }, "");
        })
    })
}

function customizeAutoFormatStrongInputTextEvent() {
    [...$$('div.strong-text input')].forEach(inputTag => {
        inputTag.addEventListener("blur", e => {
            inputTag.value = inputTag.value.trim().split(" ")
                .filter(word => word !== "")
                .map(word => word.slice(0, 1).toUpperCase() + word.slice(1).toLowerCase())
                .join(" ");
        });
    });
}

function convertStrDateToDateObj(strDate) {
    const startDateAsArr = strDate.split("/");
    return new Date(startDateAsArr[2], startDateAsArr[1] - 1, startDateAsArr[0])
}

function customizeAllAvatarColor() {
    [...$$('table tbody tr td.base-profile span.mock-avatar')].forEach(avatarTag => {
        const avatarColor = colorMap[avatarTag.innerText.trim().toUpperCase()];

        //--Convert background color to RGB
        let r = parseInt(avatarColor.slice(1, 3), 16);
        let g = parseInt(avatarColor.slice(3, 5), 16);
        let b = parseInt(avatarColor.slice(5, 7), 16);

        //--Calculate luminance
        let luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

        //--Get the right letter's color
        const letterColor = (luminance > 0.5) ? "#000000" : "#FFFFFF";

        avatarTag.style.backgroundColor = avatarColor;
        avatarTag.style.color = letterColor;
    })
}

function customizeUpdatingFormActionWhenUpdatingBtnIsClicked(updatingSupportingDataSource) {
    [...$$('div#center-page_list table tbody tr td.update a')].forEach(updatingBtn => {
        updatingBtn.addEventListener("click", e => {
            handlingCreateUpdatingForm(e.target.parentElement, updatingSupportingDataSource);
        });
    });
}

function handlingCreateUpdatingForm(updatingBtn, updatingSupportingDataSource) {
    //--Creating new 'form' and changing "action", "method" and components to correspond with updating-form.
    const newForm = updatingSupportingDataSource.plainAddingForm.cloneNode(true);
    newForm.setAttribute("action", updatingSupportingDataSource.updatingAction + updatingBtn.id);
    newForm.querySelector('div#rest-components-for-updating').outerHTML =
        updatingSupportingDataSource.componentsForUpdating.join("");
    //--Adding cancel-updating-btn at the tail of updating-form.
    newForm.innerHTML = newForm.innerHTML + '<span id="cancel-updating"><p>Huỷ cập nhật</p></span>';

    //--Mapping data-row into input and select tags of updating-form.
    const updatedObjectRow = updatingBtn.parentElement.parentElement;
    newForm.querySelectorAll('div.form-input').forEach(formInputDivBlock => {
        formInputDivBlock.querySelector('input').setAttribute("value", updatedObjectRow
            .querySelector('.' + formInputDivBlock.id)
            .textContent.trim());
    });
    newForm.querySelectorAll('div.form-select').forEach(formInputDivBlock => {
        const value = updatedObjectRow.querySelector('.' + formInputDivBlock.id).textContent.trim();
        formInputDivBlock.querySelector(`select option[value=${value}]`).selected = true;
    });
    newForm.querySelector('input[type=submit]').value = "Cập nhật";
    //--Print-out updating-form.
    $('div#center-page div#center-page_adding-form form').outerHTML = newForm.outerHTML;
    updatingSupportingDataSource.addingFormCustomizer();

    //--Customize bringing back adding-form when cancel-updating-btn is clicked.
    $('div#center-page div#center-page_adding-form form #cancel-updating')
        .addEventListener("click", e => {
            $('div#center-page div#center-page_adding-form form').outerHTML =
                updatingSupportingDataSource.plainAddingForm.outerHTML;
            updatingSupportingDataSource.addingFormCustomizer();
        });
}
