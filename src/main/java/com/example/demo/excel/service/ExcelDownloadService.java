package com.example.demo.excel.service;

import com.example.demo.excel.util.BoardExcelWriter;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;

@Service
public class ExcelDownloadService {
    private final BoardExcelWriter boardExcelWriter;

    public ExcelDownloadService(BoardExcelWriter boardExcelWriter) {
        this.boardExcelWriter = boardExcelWriter;
    }

    public void writeBoardsExcel(OutputStream os) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            boardExcelWriter.write(wb);
            wb.write(os);
            os.flush();
        }
    }
}
