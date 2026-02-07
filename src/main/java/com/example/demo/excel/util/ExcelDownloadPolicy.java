package com.example.demo.excel.util;

import com.example.demo.excel.workbook.WorkbookType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExcelDownloadPolicy {
    private final int maxRowsXssf;
    private final int maxRowsSxssf;

    public ExcelDownloadPolicy(
        @Value("${excel.download.max-rows.xssf:30000}") int maxRowsXssf,
        @Value("${excel.download.max-rows.sxssf:300000}") int maxRowsSxssf
    ) {
        this.maxRowsXssf = maxRowsXssf;
        this.maxRowsSxssf = maxRowsSxssf;
    }

    public int maxRows(WorkbookType workbookType) {
        return switch (workbookType) {
            case XSSF -> maxRowsXssf;
            case SXSSF -> maxRowsSxssf;
        };
    }
}
