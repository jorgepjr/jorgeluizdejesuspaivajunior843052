package com.musiccatalog.model;

import jakarta.persistence.*;

@Entity
public class Capa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "arquivo_hash", nullable = false)
    private String arquivoHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    protected Capa(){}

    public Capa(Album album, String arquivoHash){
        this.album = album;
        this.arquivoHash = arquivoHash;
    }

    public String getArquivoHash() {return arquivoHash;}

    public void setArquivoHash(String arquivoHash) {this.arquivoHash = arquivoHash;}

    public Album getAlbum() {return album;}

    public void setAlbum(Album album) {this.album = album;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}
}
