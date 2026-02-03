package com.musiccatalog.repository;

import com.musiccatalog.model.ArtistaAlbum;
import com.musiccatalog.model.ArtistaAlbumId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaAlbumRepository extends JpaRepository<ArtistaAlbum, ArtistaAlbumId> {
}
