package com.example.demo.excel.util;

import org.springframework.stereotype.Component;

@Component
public class BoardExcelWriter {
    public void write(Workbook wb) {
        Sheet sheet = wb.createSheet("Boards");

        int rowIdx = 0;

        // 엑셀 컬럼
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Title");
        header.createCell(2).setCellValue("Writer");
        header.createCell(3).setCellValue("ViewCount");
        header.createCell(4).setCellValue("CreatedAt");
        header.createCell(5).setCellValue("UpdatedAt");

        // 더미 1행(다운로드 연결 확인)
        Row row = sheet.createRow(rowIdx);
        row.createCell(0).setCellValue(1L);
        row.createCell(1).setCellValue("sample title");
        row.createCell(2).setCellValue("sample writer");
        row.createCell(3).setCellValue(0);
        row.createCell(4).setCellValue("2026-01-25T00:00:00");
        row.createCell(5).setCellValue("2026-01-25T00:00:00");
    }
}
