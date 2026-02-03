package com.musiccatalog.repository;

import com.musiccatalog.model.Capa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapaRepository extends JpaRepository<Capa, Long> {

    List<Capa> findByAlbumId(Long albumId);
}
