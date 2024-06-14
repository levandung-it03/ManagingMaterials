
class PdfFilesExportation {
    constructor() {
        this.fontAsBase64 = "";
    }

    async loadAllNecessaryLibs() {
        //--Load all libs for the first access.
        import("https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js")
            .then(async jspdfModule => {
                import("https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.23/jspdf.plugin.autotable.min.js")
                    .then(async tableDesignModule => {
                        import("https://cdnjs.cloudflare.com/ajax/libs/html2canvas/0.4.1/html2canvas.min.js")
                            .then(async html2canvasModule => {
                                await fetch(window.location.origin + '/src/main/resources/static/js/sources/base64-encoded-arial-font.txt')
                                    .then(response => response.text())
                                    .then(base64 => { this.fontAsBase64 = base64 })
                                    .catch(err => console.log("There's an error when loading base64font: " + err));
                            })
                            .catch(err => console.log("There's an error happened in your html2canvasModule: " + err));
                    })
                    .catch(err => console.log("There's an error happened in your tableDesignModule: " + err));
            })
            .catch(err => console.log("There's an error happened in your jspdfModule: " + err));
    }

    async fetchDataForReporter(fetchingConfigObject) {
        return fetchingConfigObject.fakeData.join("");
    }

    async buildPreviewPages(fetchingConfigObject) {
        let tablePreviewContainer = $(fetchingConfigObject.previewInfoContainer);

        //--Close component on the screen if it doesn't.
        if (!tablePreviewContainer.classList.contains('closed'))
            tablePreviewContainer.classList.add('closed');

        //--Start generating preview-page-components.
        const tableHeadCells = fetchingConfigObject.fieldObjects
            .map((field) => (`<th id="${field.cssName}">${field.utf8Name}</th>`))
            .join("");
        tablePreviewContainer.innerHTML = 
            `<div class="close-preview-page-btn">
                <i class="fa-solid fa-xmark"></i>
            </div>
            <div class="preview-page-title">
                <span>${fetchingConfigObject.tablePreviewTitle}</span>
            </div>
            <div class="preview-page-description">
                ${fetchingConfigObject.descriptionComponents.join("")}
            </div>
            <table class="exporting-table-css">
                <thead><tr>${tableHeadCells}</tr></thead>
                <tbody></tbody>
            </table>
            <div class="preview-page-statistic">
                ${fetchingConfigObject.statisticComponents.join("")}
            </div>
            <div class="report-supporting-buttons">
                <a class="report-supporting-buttons_exporting-report">
                    Xuất báo cáo&emsp;<i class="fa-solid fa-file-pdf"></i>
                </a>
            </div>`;

        //--Fetch data and put into preview table.
        await this.fetchDataForReporter(fetchingConfigObject)
            .then(rowsData => {
                tablePreviewContainer.querySelector('tbody').innerHTML = rowsData;
                tablePreviewContainer.querySelector('.close-preview-page-btn').addEventListener("click", e => {
                    tablePreviewContainer.classList.add("closed")
                });
            })
            .catch(err => console.log("Error at FetchAction and buildPreviewPages: " + err));
    }

    exportToPdfFile(tableDataSourceSelector) {
        const headData = [...$$(tableDataSourceSelector + ' thead tr th')].map(cell => cell.textContent.trim());
        const bodyData = [...$$(tableDataSourceSelector + ' tbody tr')]
            .map(rowAsDOM => [...rowAsDOM.querySelectorAll('td')].map(cellAsDOM => cellAsDOM.textContent.trim()));

        const { jsPDF } = window.jspdf;
        const doc = new jsPDF('p', 'mm', 'a4');
        const ratioLibs = { unitRatioPxAndMm: 0.22 }
        const margins = { top: 15, bottom: 10, left: 10, right: 10 };
        const constMarginTop = margins.top;
        const widthsOfPage = { portrait: 210, landscape: 500 };

        //--Set utf-8 font type for pdf-document.
        doc.addFileToVFS("Arial.ttf", this.fontAsBase64);
        doc.addFont('Arial.ttf', 'Arial', 'normal');
        doc.setFont('Arial', 'normal');

        //--Add title
        doc.setFontSize(20);
        const title = $('div.preview-page-title span');
        doc.text(title.textContent.trim(), widthsOfPage.portrait/2, margins.top, {align:"center"});

        //--Add description
        doc.setFontSize(12);
        margins.top += 10;
        [...$$('div.preview-page-description div')].forEach(description => {
            doc.text(description.textContent.trim(), 10, margins.top, {align:"left"});
            margins.top += (description.offsetHeight * ratioLibs.unitRatioPxAndMm) * 0.7;
        });

        //--Drawing table
        doc.setFontSize(8);
        doc.autoTable({
            body: bodyData,
            head: [headData],
            theme: 'grid',
            styles: { cellPadding: 1, fontSize: 10, halign: 'left', font: 'Arial', fontStyle: 'normal' },
            margin: margins,
            didDrawCell: (data) => {
                //--When the iterater reach the last cell of each row.
                if (data.column.index == headData.length - 1) {
                    //--If this line is the last line, stop halding method to prevent exception.
                    if (data.row.index + 1 >= data.table.body.length)
                        return;

                    const nextRowHeight = data.table.body[data.row.index + 1].height;
                    const curentPageHeight = doc.internal.pageSize.height;
                    const remainingPageSpace = curentPageHeight //--Total page-height.
                        - data.cursor.y //--Subtract all of drawed-row-height.
                        - data.table.body[data.row.index].height //--Subtract the standing-row (it's already drawed but the cursorY hasn't stepped yet).
                        - margins.bottom; //--Subtract the bottom-page-margin.

                    //--If remaining space on the page is less than row height
                    if (nextRowHeight > remainingPageSpace) {
                        //--Reset margins.top after drawing preview-descriptions.
                        //--Add a new page to render that row on the top of this one.
                        doc.addPage();
                        doc.setPage(doc.internal.getNumberOfPages());
                        data.cursor.y = constMarginTop; //--Set cursor to start position on new page

                        const headerHeight = data.table.head[0].height;
                        //--Build header for new-page.
                        doc.autoTable({
                            margin: margins,
                            startY: constMarginTop,
                            //--Set-up data to draw header of new-page.
                            head: [headData.map((headLabel, index) => {
                                return {
                                    content: headLabel,
                                    styles: { cellWidth: data.table.head[0].cells[index].width }
                                };
                            })],
                            theme: 'grid',
                            styles: { cellPadding: 1, fontSize: 10, halign: 'left', font: 'Arial', fontStyle: 'normal' },
                            headStyles: { minCellHeight: headerHeight }
                        });
                        //--Customize cursor after step to next page, and build header.
                        data.cursor.y = constMarginTop //--Set cursor to start position on new page
                            + headerHeight //--Set cursor at the end-point of new header to continue to draw.
                            - data.row.height; //--Set cursor back to the beginning-point of current-standing-row.
                    }
                }
            },
        });

        //--Export pdf-document.
        doc.save('example.pdf');
    }

}
