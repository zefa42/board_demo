package com.example.demo.excel.util;

import org.springframework.stereotype.Component;

@Component
public class ExcelDownloadPolicy {
    private static final int MAX_ROWS_XSSF = 30_000;
    public int maxRowsXssf() { return MAX_ROWS_XSSF; }
}
