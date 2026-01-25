package com.example.demo.excel.controller;

import com.example.demo.excel.service.ExcelDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
public class ExcelDownloadController {
    private final ExcelDownloadService excelDownloadService;

    public ExcelDownloadController(ExcelDownloadService excelDownloadService) {
        this.excelDownloadService = excelDownloadService;
    }

    @GetMapping("/boards")
    public void downloadBoards(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"boards.xlsx\"");

        excelDownloadService.writeBoardsExcel(response.getOutputStream());
    }
}
