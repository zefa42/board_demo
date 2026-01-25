package com.example.demo.excel.dto;

import java.time.LocalDateTime;

public record BoardExportQuery(
        String keyword,          // title or writer 검색용
        LocalDateTime from,      // createdAt 시작
        LocalDateTime to         // createdAt 끝
) {}