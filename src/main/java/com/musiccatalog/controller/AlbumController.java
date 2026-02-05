package com.musiccatalog.controller;

import com.musiccatalog.dto.AlbumResponse;
import com.musiccatalog.dto.CapaAlbumResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.model.Album;
import com.musiccatalog.service.AlbumService;
import com.musiccatalog.service.CapaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.musiccatalog.utils.PageableUtils.toPageable;

@Validated
@RestController
@RequestMapping("/api/v1/albuns")
@Tag(name = "3 - Albuns", description = "Endpoints para gerenciar albuns de artistas")
public class AlbumController {
    private final AlbumService albumService;
    private final CapaService capaService;

    Logger log = LoggerFactory.getLogger(AlbumController.class);

    public AlbumController(AlbumService albumService, CapaService capaService) {
        this.albumService = albumService;
        this.capaService = capaService;
    }

    @GetMapping
    @Operation(summary = "Busca com filtro", description = "Realiza busca de albuns com filtro e paginacao")
    public ResponseEntity<Page<AlbumResponse>> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TipoArtista tipoArtista,
            @RequestParam(defaultValue = "nome,asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AlbumResponse> result = albumService.filtrar(nome, tipoArtista, toPageable(page, size, sort));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Cria novo álbum", description = "Dispara notificação via WebSocket ao criar álbum")
    public ResponseEntity<AlbumResponse> criar(@RequestBody Album album) {
        var response = albumService.criar(new Album(album.getNome()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aditar artista", description = "Adita os dados do album existente")
    public ResponseEntity<AlbumResponse> editar(@PathVariable Long id, @RequestBody Album album) {
        var response = albumService.editar(id, album);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter album por ID", description = "Retorna album pelo ID")
    public ResponseEntity<AlbumResponse> obter(@NotNull @Positive @PathVariable Long id) {
        var response = albumService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover albun", description = "Remove albunm por ID")
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        albumService.excluir(id);
    }

    @GetMapping("/{albumId}/capas")
    @Operation(summary = "Obter capas", description = "Retorna links das capas por albumId")
    public ResponseEntity<CapaAlbumResponse> obterCapas(@PathVariable Long albumId) {
        CapaAlbumResponse response = capaService.obterCapa(albumId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{albumId}/artistas/{artistaId}")
    @Operation(summary = "Vincular album", description = "vincula album ao artista")
    public ResponseEntity<Void> vincularArtista(@PathVariable Long albumId, @PathVariable Long artistaId) {
        albumService.vincularArtista(albumId, artistaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{albumId}/artistas/{artistaId}")
    @Operation(summary = "Desvincular album", description = "Desvincula album do artista")
    public ResponseEntity<Void> desvincularArtista(@PathVariable Long albumId, @PathVariable Long artistaId) {
        albumService.desvincularArtista(albumId, artistaId);
        return ResponseEntity.noContent().build();
    }
}
