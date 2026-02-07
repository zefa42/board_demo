package com.example.demo.excel.util;

import com.example.demo.excel.workbook.WorkbookType;
import org.springframework.stereotype.Component;

@Component
public class ExcelDownloadPolicy {
    private static final int MAX_ROWS_XSSF = 30_000;
    private static final int MAX_ROWS_SXSSF = 300_000;

    public int maxRows(WorkbookType workbookType) {
        return switch (workbookType) {
            case XSSF -> MAX_ROWS_XSSF;
            case SXSSF -> MAX_ROWS_SXSSF;
        };
    }
}
