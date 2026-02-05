package com.musiccatalog.dto;

import com.musiccatalog.enums.TipoArtista;

public record ArtistaResponse(Long id, String nome, TipoArtista tipo) {
}
