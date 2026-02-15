package com.example.demo.excel.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExcelDownloadExceptionHandler {

    @ExceptionHandler(ExcelDownloadRowLimitExceededException.class)
    public ResponseEntity<ExcelDownloadErrorResponse> handleRowLimitExceeded(
        ExcelDownloadRowLimitExceededException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExcelDownloadErrorResponse(
                "EXCEL_ROW_LIMIT_EXCEEDED",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
            )
        );
    }

    @ExceptionHandler(ExcelDownloadConcurrencyLimitExceededException.class)
    public ResponseEntity<ExcelDownloadErrorResponse> handleConcurrencyLimitExceeded(
        ExcelDownloadConcurrencyLimitExceededException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
            new ExcelDownloadErrorResponse(
                "EXCEL_CONCURRENCY_LIMIT_EXCEEDED",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
            )
        );
    }
}
