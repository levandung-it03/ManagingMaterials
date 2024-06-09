
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
                        // await fetch('../resources/static/js/sources/base64-encoded-arial-font.txt')
                        await fetch(window.location.origin + '/js/sources/base64-encoded-arial-font.txt')
                            .then(response => response.text())
                            .then(base64 => { this.fontAsBase64 = base64 })
                            .catch(err => console.log("There's an error when loading base64font: " + err));
                    })
                    .catch(err => console.log("There's an error happened in your tableDesignModule: " + err));
            })
            .catch(err => console.log("There's an error happened in your jspdfModule: " + err));
    }

    async fetchDataForReporter(fetchingConfigObject) {
        let result = "";
        await fetch(window.location.origin + fetchingConfigObject.fetchDataAction, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(fetchingConfigObject.dataObject)
        })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error("Có lỗi xảy ra khi gửi yêu cầu.");
            })
            .then(responseObject => {
                result = responseObject.resultDataSet
                    .map(dataOfRow => {
                        for (let field in dataOfRow) if (dataOfRow[field] === null) dataOfRow[field] = "";
                        return fetchingConfigObject.rowFormattingEngine(dataOfRow);
                    })
                    .join("");
            })
            .catch(error => console.error("Đã có lỗi xảy ra:", error));

        return result;
    }

    async buildPreviewPages(fetchingConfigObject) {
        let tablePreviewContainer = $(fetchingConfigObject.tablePreviewContainerSelector);

        //--Create table-container for preview if it doesn't exist.
        if (!tablePreviewContainer.classList.contains('closed'))
            tablePreviewContainer.classList.add('closed');

        //--Start generating preview-table.
        const tableData = document.createElement("table");
        tableData.classList.add('exporting-table-css');

        //--Add exporting-btn.
        tablePreviewContainer.innerHTML = `
            <div class="report-supporting-buttons">
                <a class="report-supporting-buttons_exporting-report">
                    Xuất báo cáo&emsp;<i class="fa-solid fa-file-pdf"></i>
                </a>
            </div>`;

        //--Append the table to the tableOutPut variable (or any other container if needed).
        await this.fetchDataForReporter(fetchingConfigObject)
            .then(rowsData => {
                tableData.innerHTML = `
                    <thead><tr>${
                        fetchingConfigObject.fieldObjects.map(fieldObj => 
                            `<th id="${fieldObj.cssName}">${fieldObj.utf8Name}</th>`
                        ).join("")
                    }</tr></thead>
                    <tbody>${rowsData}</tbody>`;
                tablePreviewContainer.innerHTML = tableData.outerHTML + tablePreviewContainer.innerHTML;
            })
            .catch(err => console.log("Error at FetchAction and buildPreviewPages: " + err))
    }

    exportToPdfFile(tableDataSourceSelector) {
        const margins = { top: 10, bottom: 10, left: 5, right: 5 };
        const headData = [...$$(tableDataSourceSelector + ' thead tr th')].map(cell => cell.textContent.trim());
        const bodyData = [...$$(tableDataSourceSelector + ' tbody tr')]
            .map(rowAsDOM => [...rowAsDOM.querySelectorAll('td')].map(cellAsDOM => cellAsDOM.textContent.trim()));

        const { jsPDF } = window.jspdf;
        const doc = new jsPDF('p', 'mm', 'a4');

        //--Set utf-8 font type for pdf-document.
        doc.addFileToVFS("Arial.ttf", this.fontAsBase64);
        doc.addFont('Arial.ttf', 'Arial', 'normal');
        doc.setFont('Arial', 'normal');

        //--Drawing table
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
                        //--Add a new page to render that row on the top of this one.
                        doc.addPage();
                        doc.setPage(doc.internal.getNumberOfPages());
                        data.cursor.y = margins.top; //--Set cursor to start position on new page

                        const headerHeight = data.table.head[0].height;
                        //--Build header for new-page.
                        doc.autoTable({
                            margin: margins,
                            startY: margins.top,
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
                        data.cursor.y = margins.top //--Set cursor to start position on new page
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
