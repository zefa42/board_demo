package com.example.demo.board.controller;

import com.example.demo.board.dto.BoardCreateRequest;
import com.example.demo.board.dto.BoardDetailResponse;
import com.example.demo.board.dto.BoardListResponse;
import com.example.demo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody BoardCreateRequest request) {
        long id = boardService.create(request.getTitle(), request.getContent(), request.getWriter());
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BoardCreateRequest request) {
        boardService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam String writer) {
        boardService.delete(id, writer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<BoardListResponse> getBoardList(@PageableDefault(size=10, sort = "id") Pageable pageable) {
        return boardService.getBoardList(pageable);
    }

    @GetMapping("/{id}")
    public BoardDetailResponse getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }
}
