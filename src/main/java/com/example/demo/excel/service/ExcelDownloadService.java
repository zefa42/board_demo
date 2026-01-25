package com.example.demo.excel.service;

import com.example.demo.excel.dto.BoardExportQuery;
import com.example.demo.excel.repository.BoardRepositoryCustom;
import com.example.demo.excel.util.BoardExcelWriter;
import com.example.demo.excel.util.ExcelDownloadLimiter;
import com.example.demo.excel.util.ExcelDownloadPolicy;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelDownloadService {
    private final BoardExcelWriter boardExcelWriter;
    private final BoardRepositoryCustom boardRepository;
    private final ExcelDownloadPolicy policy;
    private final ExcelDownloadLimiter limiter;

    private static final Logger log = LoggerFactory.getLogger(ExcelDownloadService.class);

    public ExcelDownloadService(BoardExcelWriter boardExcelWriter,
                                BoardRepositoryCustom boardRepository,
                                ExcelDownloadPolicy policy,
                                ExcelDownloadLimiter limiter) {
        this.boardExcelWriter = boardExcelWriter;
        this.boardRepository = boardRepository;
        this.policy = policy;
        this.limiter = limiter;
    }

    public void writeBoardsExcel(OutputStream os) throws IOException {
        long start = System.currentTimeMillis();

        limiter.runWithLimit(() -> {
            BoardExportQuery query = new BoardExportQuery(null, null, null);

            long total = boardRepository.countForExport(query);
            if (total > policy.maxRowsXssf()) {
                log.warn("excel_download boards rejected: total={}", total);
                throw new IllegalArgumentException("XSSF 처리 시 스레드풀 위험. 범위를 줄여주세요");
            }

            try (Workbook wb = new XSSFWorkbook()) {
                boardExcelWriter.write(wb);
                wb.write(os);
                os.flush();
            }

            long ms = System.currentTimeMillis() - start;
            log.info("excel_download boards success: total={} 소요시간={}", total, ms);
            return null;
        });
    }
}
