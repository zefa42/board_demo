package com.example.demo.excel.exception;

public class ExcelDownloadRowLimitExceededException extends RuntimeException {
    public ExcelDownloadRowLimitExceededException(String message) {
        super(message);
    }
}
