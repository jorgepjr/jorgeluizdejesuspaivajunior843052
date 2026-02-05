package com.musiccatalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artista_album")
public class ArtistaAlbum {

    @EmbeddedId
    private ArtistaAlbumId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistaId")
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("albumId")
    @JoinColumn(name = "album_id")
    private Album album;

    public ArtistaAlbum() {}

    public ArtistaAlbum(Artista artista, Album album) {
        this.id = new ArtistaAlbumId(artista.getId(), album.getId());
        this.artista = artista;
        this.album = album;
    }

    public ArtistaAlbumId getId() {return id;}

    public void setId(ArtistaAlbumId id) {this.id = id;}

    public Artista getArtista() {return artista;}

    public void setArtista(Artista artista) {this.artista = artista;}

    public Album getAlbum() {return album;}

    public void setAlbum(Album album) {this.album = album;}
}
