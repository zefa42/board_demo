package com.example.demo.board.service;

import com.example.demo.board.dto.BoardCreateRequest;
import com.example.demo.board.dto.BoardDetailResponse;
import com.example.demo.board.dto.BoardListResponse;
import com.example.demo.board.entity.Board;
import com.example.demo.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public long create(String title, String content, String writer) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();

        return boardRepository.save(board).getId();
    }

    public void update(Long id, BoardCreateRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        //작성자 동일 여부 확인
        if(!request.getWriter().equals(board.getWriter())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        board.update(request.getTitle(), request.getContent());
    }

    public void delete(Long id, String writer) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        //작성자 동일 여부 확인
        if(!writer.equals(board.getWriter())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }

    public Page<BoardListResponse> getBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardListResponse::from);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return BoardDetailResponse.from(board);
    }
}
