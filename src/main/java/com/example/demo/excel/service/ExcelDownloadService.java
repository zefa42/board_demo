package com.example.demo.excel.service;

import com.example.demo.excel.dto.BoardExportQuery;
import com.example.demo.excel.repository.BoardRepositoryCustom;
import com.example.demo.excel.util.BoardExcelWriter;
import com.example.demo.excel.util.ExcelDownloadLimiter;
import com.example.demo.excel.util.ExcelDownloadPolicy;
import com.example.demo.excel.workbook.ExcelWorkbookFactory;
import com.example.demo.excel.workbook.WorkbookType;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelDownloadService {
    private final BoardExcelWriter boardExcelWriter;
    private final BoardRepositoryCustom boardRepository;
    private final ExcelDownloadPolicy policy;
    private final ExcelDownloadLimiter limiter;
    private final ExcelWorkbookFactory workbookFactory;

    private static final Logger log = LoggerFactory.getLogger(ExcelDownloadService.class);

    public ExcelDownloadService(BoardExcelWriter boardExcelWriter,
                                BoardRepositoryCustom boardRepository,
                                ExcelDownloadPolicy policy,
                                ExcelDownloadLimiter limiter,
                                ExcelWorkbookFactory workbookFactory) {
        this.boardExcelWriter = boardExcelWriter;
        this.boardRepository = boardRepository;
        this.policy = policy;
        this.limiter = limiter;
        this.workbookFactory = workbookFactory;
    }

    public void writeBoardsExcel(OutputStream os) throws IOException {
        long start = System.currentTimeMillis();

        limiter.runWithLimit(() -> {
            BoardExportQuery query = new BoardExportQuery(null, null, null);
            WorkbookType workbookType = workbookFactory.workbookType();
            int maxRows = policy.maxRows(workbookType);

            long total = boardRepository.countForExport(query);
            if (total > maxRows) {
                log.warn("excel_download boards rejected: type={}, total={}, maxRows={}", workbookType, total, maxRows);
                throw new IllegalArgumentException(
                    String.format("엑셀 다운로드 허용 행수(%d건)를 초과했습니다. 조회 범위를 줄여주세요.", maxRows)
                );
            }

            try (Workbook wb = workbookFactory.create()) {
                boardExcelWriter.write(wb);
                wb.write(os);
                os.flush();
            }

            long ms = System.currentTimeMillis() - start;
            log.info("excel_download boards success: type={}, total={}, elapsedMs={}", workbookType, total, ms);
            return null;
        });
    }
}
