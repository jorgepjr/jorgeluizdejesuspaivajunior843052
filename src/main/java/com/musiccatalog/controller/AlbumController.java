package com.musiccatalog.controller;

import com.musiccatalog.dto.PagedResponse;
import com.musiccatalog.model.Album;
import com.musiccatalog.service.AlbumService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumController {
    private final AlbumService service;

    public AlbumController(AlbumService service) {this.service = service;}

    @GetMapping
    public PagedResponse<Album> obterPaginado(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return service.obterPaginado(pageNumber, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album criar(@RequestBody Album album){
        return service.criar(new Album(album.getNome()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album editar(@PathVariable Long id, @RequestBody Album album) {
        return service.editar(id, album);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album obter(@NotNull @Positive @PathVariable Long id) {
        return service.obterPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
       service.excluir(id);
    }
}
