package com.example.demo.excel.controller;

import com.example.demo.excel.service.ExcelDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        response.setHeader("Content-Disposition", buildContentDisposition("게시글 목록.xlsx", "boards.xlsx"));

        excelDownloadService.writeBoardsExcel(response.getOutputStream());
    }

    private String buildContentDisposition(String utf8FileName, String asciiFallbackFileName) {
        String encoded = URLEncoder.encode(utf8FileName, StandardCharsets.UTF_8).replace("+", "%20");
        return "attachment; filename=\"" + asciiFallbackFileName + "\"; filename*=UTF-8''" + encoded;
    }
}
