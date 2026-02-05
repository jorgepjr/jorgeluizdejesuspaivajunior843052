package com.musiccatalog.repository;

import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("""
    SELECT DISTINCT al
    FROM Album al
    LEFT JOIN FETCH al.capas c
    LEFT JOIN FETCH al.artistas aa
    LEFT JOIN FETCH aa.artista ar
    WHERE (:tipoArtista IS NULL OR ar.tipo = :tipoArtista)
      AND (:nome IS NULL OR :nome = ''
           OR UPPER(ar.nome) LIKE CONCAT('%', UPPER(:nome), '%')
           OR UPPER(al.nome) LIKE CONCAT('%', UPPER(:nome), '%'))
""")
    Page<Album> filtrar(@Param("nome") String busca, @Param("tipoArtista") TipoArtista tipo, Pageable pageable);

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Album> findByNomeIgnoreCase(String nome);


}
