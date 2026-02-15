package com.example.demo.excel.exception;

import java.time.LocalDateTime;

public record ExcelDownloadErrorResponse(
    String code,
    String message,
    String path,
    LocalDateTime timestamp
) {
}
