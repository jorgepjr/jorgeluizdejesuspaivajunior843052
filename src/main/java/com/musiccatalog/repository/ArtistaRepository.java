package com.musiccatalog.repository;

import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.model.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    @Query("""
            SELECT a
            FROM Artista a
            WHERE (:nome IS NULL OR :nome = '' OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
              AND (:tipo IS NULL OR a.tipo = :tipo)
            """)
    Page<Artista> filtrar(@Param("nome") String nome, @Param("tipo") TipoArtista tipo, Pageable pageable
    );

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Artista> findByNomeIgnoreCase(String nome);

}
