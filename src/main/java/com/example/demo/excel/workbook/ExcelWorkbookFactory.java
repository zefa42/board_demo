package com.example.demo.excel.workbook;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelWorkbookFactory {
    Workbook create();

    WorkbookType workbookType();
}
