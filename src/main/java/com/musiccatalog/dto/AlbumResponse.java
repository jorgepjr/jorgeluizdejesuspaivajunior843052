package com.musiccatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Retorno da criação do album")
public record AlbumResponse(
        Long id,
        String nome,
        List<ArtistaResponse> artistas,
        List<CapaResponse> capas
) {
}
