package com.musiccatalog.model;

import jakarta.persistence.JoinColumn;

public class Capa {

    private Long id;
    private String imagemUrl;

    @JoinColumn(name = "album_id")
    private Album album;
}
