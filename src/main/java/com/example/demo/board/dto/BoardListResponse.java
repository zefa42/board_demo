package com.example.demo.board.dto;

import com.example.demo.board.entity.Board;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardListResponse {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    public static BoardListResponse from(Board board) {
        return BoardListResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
