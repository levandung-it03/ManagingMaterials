const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const paginationSize = 1;
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
let tempCounter = 0;

function tst() {
    tempCounter++;
    console.log(tempCounter);
}

function log(v) {
    console.log(v);
}

function salaryFormattingEngine(salary) {
    const salaryAsString = salary + "";
    let result = "";
    for (let index = salaryAsString.length; index > 0; index -= 3)
        result = "," + salaryAsString.substring(index - 3, index) + result;
    return result.slice(1) + "VNĐ";
}

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
            else $('span#' + elem[0]).style.display = "inline";
        };
        elem[1].tag.addEventListener("keyup", e => toggleShowMessage(elem));
        elem[1].tag.addEventListener("change", e => toggleShowMessage(elem));
    });
}

function customizeSubmitFormAction(formSelector, validatingBlocks) {
    $(formSelector).onsubmit = e => {
        if (confirm("Bạn chắc chắn muốn thực hiện thao tác?") === true) {
            let isValid = Object.entries(validatingBlocks)
                .every(elem => {
                    elem[1].tag.value = elem[1].tag.value.trim();
                    return elem[1].validate(elem[1].tag.value);
                });
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
                e.target.parentElement.parentElement.querySelector('input').type = "text";
            } else {
                e.target.classList.add("hidden");
                e.target.parentElement.querySelector(".show-pass").classList.remove("hidden");
                e.target.parentElement.parentElement.querySelector('input').type = "password";
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

function paintAllAvatarColor() {
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

function customizeSortingListEvent() {
    [...$$('table thead th i')].forEach(btn => {
        btn.addEventListener("click", e => {
            const fieldId = e.target.parentElement.id;
            const cellOfFields = [...$$('table tbody td.' + fieldId)];
            const firstCellOfSearchingColumn = cellOfFields[0].getAttribute('plain-value');
            let searchingDataFieldType = null;

            if (Number.parseInt(firstCellOfSearchingColumn) !== null) searchingDataFieldType = "Number";
            else if (new Date(firstCellOfSearchingColumn) !== null) searchingDataFieldType = "Date";
            else searchingDataFieldType = "String";

            cellOfFields.sort((a, b) => {
                const firstCell = a.getAttribute('plain-value');
                const secondCell = b.getAttribute('plain-value');

                if (searchingDataFieldType === "Number") return Number.parseInt(firstCell) - Number.parseInt(secondCell);
                else if (searchingDataFieldType === "Date") return new Date(firstCell) < new Date(secondCell);
                else return firstCell.localeCompare(secondCell);
            });
            alert("Sắp xếp thành công!");
            $('table tbody').innerHTML = cellOfFields.reduce((accumulator, cell) => {
                return accumulator + cell.parentElement.outerHTML;
            }, "");
        })
    })
}

function customizeSearchingListEvent(searchingSupportingDataSource) {
    const searchingInputTag = $('#table-search-box input#search');
    const selectedOption = $('#table-search-box select#search');

    const handleSearchingListEvent = async e => {
        //--Stop searching if searched-field is not selected yet.
        if (selectedOption.value === "") alert("Bạn hãy chọn trường cần tìm kiếm trước!");

        //--Start data with selected field by calling an API.
        else {
            searchingSupportingDataSource.data.searchingValue = searchingInputTag.value;
            searchingSupportingDataSource.data.searchingField = selectedOption.value;

            //-- Set pagination-bar[page-1] before "fetch" to make the printed-result start from [1].
            searchingSupportingDataSource.data.currentPage = 1;

            //--Use await to make this "fetch" action sync with this "handleSearchingListEvent" method.
            await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);
        }
        return null;
    }

    $('#table-search-box i').addEventListener("click", handleSearchingListEvent);
    searchingInputTag.addEventListener("keyup", handleSearchingListEvent);
}

async function fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource) {
    await fetch(
        window.location.origin + searchingSupportingDataSource.fetchDataAction,
        {//--Request Options
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(
                /**
                 * Data Base Structure:
                 * data: {
                 * currentPage: 1,
                 * objectsQuantity: 1,
                 * searchingField: "orderId",
                 * searchingValue: "",
                 * ...more_custom_fields...
                 *}
                 * */
                searchingSupportingDataSource.data
            )
        }
    )
        .then(response => {
            if (response.ok) return response.json();
            else throw new Error("Có lỗi xảy ra khi gửi yêu cầu.");
        })
        .then(responseObject => {
            if (responseObject["totalObjectsQuantityResult"] === 0) {
                searchingSupportingDataSource.tableBody.innerHTML =
                    '<tr><td style="width: 100%">Không tìm thấy dữ liệu vừa nhập</td></tr>';
            } else {
                //--Render table-data by found result-data-set.
                searchingSupportingDataSource.tableBody.innerHTML = responseObject["resultDataSet"]
                    .map(dataOfRow => {
                        for (let field in dataOfRow) if (dataOfRow[field] === null) dataOfRow[field] = "";
                        return searchingSupportingDataSource.rowFormattingEngine(dataOfRow);
                    })
                    .join("");
            }
            //--Update objects-quantity value of hidden-input-tag to build pagination-bar.
            //--Maybe "0" if data-set is empty.
            searchingSupportingDataSource.data.objectsQuantity = responseObject["totalObjectsQuantityResult"];
        })
        .catch(error => {
            console.error("Đã có lỗi xảy ra:", error)
        });
}

function generatePaginationBar(observedTableContainer, searchingSupportingDataSource) {
    (function customizePaginationBar() {
        const paginationBarSelector = observedTableContainer + ' div.table-footer_main';
        const indexNumberFormatter = (page) => `<span class="interact-page-btn page-${page}">${page}</span>`;
        const dotsSeparatorBlock = '<span style="align-self: end; padding: 0 10px; font-size: 1.4rem;">...</span>';

        //--Temporary empty result.
        let indexNumberBlocks = '';
        const totalPages = Math.ceil(searchingSupportingDataSource.data.objectsQuantity / paginationSize);

        //--case.1 - [1 2 3 4 5 6 7]
        if (totalPages <= 7) {
            for (let index = 1; index <= totalPages; index++) {
                //--Loop from [1] to the [current-selected-page-number + 2].
                //--Note: the range [+ 2] is used for user-selecting-benefit.
                //--Note: max of this indexNumbers is [7], so we don't need to add "..." into this bar.
                indexNumberBlocks += indexNumberFormatter(index);
            }

            //--Build the pagination-bar-constructor.
            $(paginationBarSelector).innerHTML = `<div id="index-numbers">${indexNumberBlocks}</div>`;
        }
            //--case.2 - [<] [1] 2 3 ... 15 [>]
        //--Note: the [15] "max-page-number" is just an example, use the "total-pages" instead.
        else {
            //--case.2.1 - [<] 1 2 3 [4] 5 6 ... 15 [>]
            if (searchingSupportingDataSource.data.currentPage <= 4) {
                //--Build the child part - [<] 1 2 3 [4] 5 6 ____
                for (let index = 1; index <= (searchingSupportingDataSource.data.currentPage + 2); index++) {
                    //--Loop from [1] to the [current-selected-page-number + 2].
                    //--Note: the range [+ 2] is used for user-selecting-benefit.
                    indexNumberBlocks += indexNumberFormatter(index);
                }

                //--Build the child part - ____ ... 15 [>]
                indexNumberBlocks += (dotsSeparatorBlock + indexNumberFormatter(totalPages));
            }
            //--case.2.2 - [<] 1 ... 10 11 [12] 13 14 15 [>]
            else if ((totalPages - searchingSupportingDataSource.data.currentPage) <= 4) {
                //--Build the child part - [<] 1 ... ____
                indexNumberBlocks += (indexNumberFormatter(1) + dotsSeparatorBlock);

                //--Build the child part - ____ 10 11 [12] 13 14 15 [>]
                for (let index = (searchingSupportingDataSource.data.currentPage - 2); index <= totalPages; index++) {
                    //--Loop from the [current-selected-page-number - 2] to [15].
                    //--Note: the range [- 2] is used for user-selecting-benefit.
                    indexNumberBlocks += indexNumberFormatter(index);
                }
            }
            //--case.2.3 - [<] 1 ... 3 4 [5] 6 7 ... 15 [>]
            else {
                //--Build the child part - [<] 1 ... ____
                indexNumberBlocks += (indexNumberFormatter(1) + dotsSeparatorBlock);

                //--Build the child part - ____ 3 4 [5] 6 7 ____
                for (let index = (searchingSupportingDataSource.data.currentPage - 2);
                     index <= (searchingSupportingDataSource.data.currentPage + 2); index++) {
                    //--Loop from the [current-selected-page-number - 2] to [current-selected-page-number - 2].
                    //--Note: the range [- 2], [+ 2] is used for user-selecting-benefit.
                    indexNumberBlocks += indexNumberFormatter(index);
                }
                //--Build the child part - ____ ... 15 [>]
                indexNumberBlocks += (dotsSeparatorBlock + indexNumberFormatter(totalPages));
            }

            //--Build the pagination-bar-constructor.
            $(paginationBarSelector).innerHTML = `
                <span id="page-moving-previous-btn" class="interact-page-btn"><i class="fa-solid fa-angle-left"></i></span>
                <div id="index-numbers">${indexNumberBlocks}</div>
                <span id="page-moving-next-btn" class="interact-page-btn"><i class="fa-solid fa-angle-right"></i></span>
            `;
            //--Check if user's touching the limit of index-number-pages.
            //--Note: "deactivated" class make the moving-btn can't click.
            //--Note: "deactivated" class make the btn can't complete the "fetch" action.
            const nextBtn = $('span#page-moving-next-btn');
            const previousBtn = $('span#page-moving-previous-btn');
            if (nextBtn !== null && searchingSupportingDataSource.data.currentPage === totalPages)
                nextBtn.classList.add("deactivated");
            else if (previousBtn !== null && searchingSupportingDataSource.data.currentPage === 1)
                previousBtn.classList.add("deactivated");
        }

        const selectedIndexBtn = $(`${paginationBarSelector} div#index-numbers span.page-${searchingSupportingDataSource.data.currentPage}`);
        //--Note: "selected-page" class make the index-btn be highlighted.
        //--Note: "deactivated" class make the btn can't complete the "fetch" action.
        if (selectedIndexBtn !== null)
            selectedIndexBtn.classList.add("selected-page", "deactivated");
    })();

    (function customizeSelectingInteractivePageButtons() {
        $$('span.interact-page-btn').forEach(btn => {
            btn.addEventListener("click", async e => {
                if (!btn.classList.contains("deactivated")) {
                    //--If the selected btn is not the index-number-page-buttons.
                    if (btn.id.trim() === "page-moving-previous-btn") searchingSupportingDataSource.data.currentPage--;
                    else if (btn.id.trim() === "page-moving-next-btn") searchingSupportingDataSource.data.currentPage++;
                    //--Or else.
                    else searchingSupportingDataSource.data.currentPage = Number.parseInt(btn.textContent.trim());

                    //--Starting fetch data by selected-pagination-bar-buttons.
                    await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource);
                }
            });
        });
    })();
}

function customizeGeneratingFormUpdateEvent(observedTableContainer, updatingSupportingDataSource) {
    [...$$(observedTableContainer + ' tbody tr td.update a')].forEach(updatingBtn => {
        updatingBtn.addEventListener("click", e => {
            handlingCreateFormUpdate(e.target.parentElement, updatingSupportingDataSource);
        });
    });
}

function handlingCreateFormUpdate(updatingBtn, updatingSupportingDataSource) {
    //--Creating new 'form' and changing "action", "method" and components to correspond with updating-form.
    const newForm = updatingSupportingDataSource.plainAddingForm.cloneNode(true);
    newForm.setAttribute("action", updatingSupportingDataSource.updatingAction);
    //--Adding rest-components needed for updating.
    const restComponents = newForm.querySelector('div#rest-components-for-updating');
    if (restComponents !== null)
        restComponents.outerHTML = updatingSupportingDataSource.componentsForUpdating.join("");
    //--Adding cancel-updating-btn at the tail of updating-form.
    newForm.innerHTML = newForm.innerHTML + '<span id="cancel-updating"><p>Huỷ cập nhật</p></span>';

    //--Mapping data-row into input and select tags of updating-form.
    const updatedObjectRow = updatingBtn.parentElement.parentElement;
    const allBlocksContainInputTag = newForm.querySelectorAll('div.form-input');
    if (allBlocksContainInputTag !== null)
        allBlocksContainInputTag.forEach(formInputDivBlock => {
            formInputDivBlock.querySelector('input').setAttribute("value", updatedObjectRow
                .querySelector('.' + formInputDivBlock.id)
                .getAttribute('plain-value').trim()
            );
        });
    const allBlocksContainSelectTag = newForm.querySelectorAll('div.form-select');
    if (allBlocksContainSelectTag !== null)
        allBlocksContainSelectTag.forEach(formInputDivBlock => {
            formInputDivBlock.querySelector(`select option[value=${
                updatedObjectRow.querySelector('.' + formInputDivBlock.id).textContent.trim()
            }]`).selected = true;
        });

    //--Set up the rest components of updating-form.
    newForm.querySelector('input[type=submit]').value = "Cập nhật";
    newForm.querySelector('input[name*="Id"]').readOnly = true;
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

async function CustomizeToggleOpeningAddingFormDialogSupporter(
    searchingSupportingDataSource,
    addingFormDialogSupporterSelector='div#select-dialog'
) {
    const selectDialog = $(addingFormDialogSupporterSelector);
    //--Open dialog when clicking on icon in the add form
    $(`div#center-page_adding-form #${searchingSupportingDataSource.data.searchingField} i`)
        .addEventListener("click", async e => {
            await fetchingPaginatedDataAndMapIntoTable(searchingSupportingDataSource)
            //--Open select-dialog.
            selectDialog.classList.remove("closed");
        });

    //--Auto fill input value when clicking on any row in select dialog
    $(addingFormDialogSupporterSelector + ` tbody`).addEventListener("click", e =>{
        $(`div#center-page_adding-form #${searchingSupportingDataSource.data.searchingField} input`)
            .value = e.target.closest("tr").id;
        selectDialog.classList.add("closed");
    });

    //--Customize closing dialog action.
    //--Stop propagation when clicking on dialog container so when select from list, it won't close the dialog
    selectDialog.querySelector('div.select-dialog-container').addEventListener("click", e => e.stopPropagation())
    //--Close dialog when clicking on dialog
    selectDialog.addEventListener("click", () => selectDialog.classList.add("closed"));
    //--Close dialog when clicking on close button
    selectDialog.querySelector('div.closing-dialog-btn').addEventListener("click", ()=> selectDialog.classList.add("closed"))

    await CustomizeFetchingActionSpectator(
        searchingSupportingDataSource,
        {
            callModulesOfExtraFeatures: () => {}
        },
        addingFormDialogSupporterSelector
    );
}

async function CustomizeFetchingActionSpectator(
    searchingSupportingDataSource,
    moreFeatures,
    observedTableContainer = 'div#center-page_list'
) {
    //--Create a mutation observer instance when each fetch-action is made.
    await new MutationObserver(async () => {
        //--Re-calculate the quantities.
        if (moreFeatures.tableLabel) {
            $(observedTableContainer + ' #quantity').textContent =
                $$(observedTableContainer + ' tbody tr').length + " " + moreFeatures.tableLabel;
        }

        //--Rebuild pagination-bar.
        generatePaginationBar(observedTableContainer, searchingSupportingDataSource);

        //--Call all rest custom modules.
        await moreFeatures.callModulesOfExtraFeatures();

        //--Configure the observer to observe changes to the table's child list
    }).observe($(observedTableContainer + ' tbody'), {childList: true, subtree: true});
}
