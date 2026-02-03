package com.musiccatalog.model;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class ArtistaAlbumId implements Serializable {

    @Column(name = "artista_id")
    private Long artistaId;

    @Column(name = "album_id")
    private Long albumId;

    protected ArtistaAlbumId() {}

    public ArtistaAlbumId(Long artistaId, Long albumId) {
        this.artistaId = artistaId;
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistaAlbumId that)) return false;
        return Objects.equals(artistaId, that.artistaId) &&
                Objects.equals(albumId, that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistaId, albumId);
    }
}
