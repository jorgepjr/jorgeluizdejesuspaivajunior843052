package com.musiccatalog.controller;

import com.musiccatalog.dto.AlbumResponse;
import com.musiccatalog.dto.CapaAlbumResponse;
import com.musiccatalog.model.Album;
import com.musiccatalog.service.AlbumService;
import com.musiccatalog.service.CapaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.musiccatalog.utils.PageableUtils.toPageable;

@Validated
@RestController
@RequestMapping("/api/v1/albuns")
@Tag(name = "Albuns", description = "Endpoints para gerenciar albuns de artistas")
public class AlbumController {
    private final AlbumService albumService;
    private final CapaService capaService;

    public AlbumController(AlbumService albumService, CapaService capaService) {
        this.albumService = albumService;
        this.capaService = capaService;
    }

    @GetMapping
    public ResponseEntity<Page<AlbumResponse>> filtrar(
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "nome,asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        Page<AlbumResponse> result = albumService.filtrar(busca, toPageable(page, size, sort));
        return ResponseEntity.ok(result);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album criar(@RequestBody Album album) {
        return albumService.criar(new Album(album.getNome()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album editar(@PathVariable Long id, @RequestBody Album album) {
        return albumService.editar(id, album);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album obter(@NotNull @Positive @PathVariable Long id) {
        return albumService.obterPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        albumService.excluir(id);
    }

    @GetMapping("/{albumId}/capas")
    public ResponseEntity<CapaAlbumResponse> obterCapas(@PathVariable Long albumId) {
        CapaAlbumResponse response = capaService.obterCapa(albumId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{albumId}/artistas/{artistaId}")
    public ResponseEntity<Void> vincularArtista(@PathVariable Long albumId, @PathVariable Long artistaId) {
        albumService.vincularArtista(albumId, artistaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{albumId}/artistas/{artistaId}")
    public ResponseEntity<Void> desvincularArtista(@PathVariable Long albumId, @PathVariable Long artistaId) {
        albumService.desvincularArtista(albumId, artistaId);
        return ResponseEntity.noContent().build();
    }
}
