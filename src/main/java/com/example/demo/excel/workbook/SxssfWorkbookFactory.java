package com.example.demo.excel.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("excel-sxssf")
public class SxssfWorkbookFactory implements ExcelWorkbookFactory {
    @Override
    public Workbook create() {
        SXSSFWorkbook wb = new SXSSFWorkbook(200);
        wb.setCompressTempFiles(true);
        return wb;
    }

    @Override
    public WorkbookType workbookType() {
        return WorkbookType.SXSSF;
    }
}
