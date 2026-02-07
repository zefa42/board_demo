package com.example.demo.excel.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!excel-sxssf")
public class XssfWorkbookFactory implements ExcelWorkbookFactory {
    @Override
    public Workbook create() {
        return new XSSFWorkbook();
    }

    @Override
    public WorkbookType workbookType() {
        return WorkbookType.XSSF;
    }
}
