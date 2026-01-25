package com.example.demo.excel.repository.implement;

import com.example.demo.board.entity.Board;
import com.example.demo.excel.dto.BoardExportQuery;
import com.example.demo.excel.repository.BoardRepositoryCustom;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final EntityManager em;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Board> findChunk(BoardExportQuery q, int offset, int limit) {
        String jpql = """
            select b from Board b
            where (:kw is null or b.title like concat('%', :kw, '%') or b.writer like concat('%', :kw, '%'))
              and (:from is null or b.createdAt >= :from)
              and (:to is null or b.createdAt < :to)
            order by b.id asc
        """;

        return em.createQuery(jpql, Board.class)
                .setParameter("kw", q.keyword())
                .setParameter("from", q.from())
                .setParameter("to", q.to())
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countForExport(BoardExportQuery q) {
        String jpql = """
            select count(b) from Board b
            where (:kw is null or b.title like concat('%', :kw, '%') or b.writer like concat('%', :kw, '%'))
              and (:from is null or b.createdAt >= :from)
              and (:to is null or b.createdAt < :to)
        """;

        return em.createQuery(jpql, Long.class)
                .setParameter("kw", q.keyword())
                .setParameter("from", q.from())
                .setParameter("to", q.to())
                .getSingleResult();
    }
}
