package com.musiccatalog.repository;

import com.musiccatalog.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Page<Album> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("""
            SELECT DISTINCT al
            FROM Album al
            LEFT JOIN FETCH al.capas c
            LEFT JOIN FETCH al.artistas aa
            LEFT JOIN FETCH aa.artista ar
            WHERE (:busca IS NULL OR ar.tipo = :busca)
            OR    (:busca IS NULL OR ar.nome = :busca)
            OR    (:busca IS NULL OR al.nome = :busca)
            """)
    Page<Album> filtrar(@Param("busca") String busca, Pageable pageable);

}
