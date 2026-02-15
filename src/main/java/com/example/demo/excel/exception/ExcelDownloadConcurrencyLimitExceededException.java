package com.example.demo.excel.exception;

public class ExcelDownloadConcurrencyLimitExceededException extends RuntimeException {
    public ExcelDownloadConcurrencyLimitExceededException(String message) {
        super(message);
    }
}
