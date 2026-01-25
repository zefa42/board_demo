package com.example.demo.excel.repository;

import com.example.demo.board.entity.Board;
import com.example.demo.excel.dto.BoardExportQuery;
import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findChunk(BoardExportQuery query, int offset, int limit);
    long countForExport(BoardExportQuery query);
}
