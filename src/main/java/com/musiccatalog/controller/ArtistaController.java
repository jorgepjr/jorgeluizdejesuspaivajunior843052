package com.musiccatalog.controller;

import com.musiccatalog.dto.PagedResponse;
import com.musiccatalog.model.Artista;
import com.musiccatalog.service.ArtistaService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaController {
    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {this.service = service;}

    @GetMapping
    public PagedResponse<Artista> obterPaginado(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return service.obterPaginado(pageNumber, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Artista criar(@RequestBody Artista artista){
        return service.criar(new Artista(artista.getNome(), artista.getTipo()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artista editar(@PathVariable Long id, @RequestBody Artista artista) {
        return service.editar(id, artista);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artista obter(@NotNull @Positive @PathVariable Long id) {
        return service.obterPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
       service.excluir(id);
    }
}
