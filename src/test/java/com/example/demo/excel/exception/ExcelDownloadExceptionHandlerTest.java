package com.example.demo.excel.exception;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExcelDownloadExceptionHandlerTest {

    private final ExcelDownloadExceptionHandler handler = new ExcelDownloadExceptionHandler();

    @Test
    void shouldReturnBadRequestForRowLimitExceeded() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/excel/boards");

        ResponseEntity<ExcelDownloadErrorResponse> response = handler.handleRowLimitExceeded(
            new ExcelDownloadRowLimitExceededException("행수 초과"),
            request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("EXCEL_ROW_LIMIT_EXCEEDED");
        assertThat(response.getBody().message()).isEqualTo("행수 초과");
        assertThat(response.getBody().path()).isEqualTo("/excel/boards");
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    void shouldReturnTooManyRequestsForConcurrencyLimitExceeded() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/excel/boards");

        ResponseEntity<ExcelDownloadErrorResponse> response = handler.handleConcurrencyLimitExceeded(
            new ExcelDownloadConcurrencyLimitExceededException("동시 요청 초과"),
            request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("EXCEL_CONCURRENCY_LIMIT_EXCEEDED");
        assertThat(response.getBody().message()).isEqualTo("동시 요청 초과");
        assertThat(response.getBody().path()).isEqualTo("/excel/boards");
        assertThat(response.getBody().timestamp()).isNotNull();
    }
}
