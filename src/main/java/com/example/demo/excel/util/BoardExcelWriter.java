package com.example.demo.excel.util;

import com.example.demo.board.entity.Board;
import com.example.demo.excel.dto.BoardExportQuery;
import com.example.demo.excel.repository.BoardRepositoryCustom;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class BoardExcelWriter {
    private final BoardRepositoryCustom boardRepository;

    public BoardExcelWriter(BoardRepositoryCustom boardRepository) {
        this.boardRepository = boardRepository;
    }

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
//        Row row = sheet.createRow(rowIdx);
//        row.createCell(0).setCellValue(1L);
//        row.createCell(1).setCellValue("sample title");
//        row.createCell(2).setCellValue("sample writer");
//        row.createCell(3).setCellValue(0);
//        row.createCell(4).setCellValue("2026-01-25T00:00:00");
//        row.createCell(5).setCellValue("2026-01-25T00:00:00");

        // 예시: 전체 다운로드 (원하면 Controller에서 query를 받아 넘기도록 변경)
        BoardExportQuery query = new BoardExportQuery(null, null, null);

        int page = 0;
        int size = 5000;

        while (true) {
            int offset = page * size;
            List<Board> chunk = boardRepository.findChunk(query, offset, size);
            if (chunk.isEmpty()) break;

            for (Board b : chunk) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getTitle());
                row.createCell(2).setCellValue(b.getWriter());
                row.createCell(3).setCellValue(b.getViewCount());
                row.createCell(4).setCellValue(String.valueOf(b.getCreatedAt()));
                row.createCell(5).setCellValue(String.valueOf(b.getUpdatedAt()));
            }
            page++;
        }
    }
}
