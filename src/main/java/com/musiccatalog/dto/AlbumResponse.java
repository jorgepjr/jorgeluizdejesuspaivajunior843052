package com.musiccatalog.dto;

import java.util.List;

public record AlbumResponse(
        Long id,
        String nome,
        List<ArtistaResponse> artistas,
        List<CapaResponse> capas
) {
}
