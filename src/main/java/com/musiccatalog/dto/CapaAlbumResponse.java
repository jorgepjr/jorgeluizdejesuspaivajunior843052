package com.musiccatalog.dto;

import java.util.List;

public record CapaAlbumResponse(Long albumId, String nome, List<String> linksCapas) {
}
