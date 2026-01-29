package com.musiccatalog.controller;

import com.musiccatalog.model.Artista;
import com.musiccatalog.service.ArtistaService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaController {
    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {this.service = service;}

    @PostMapping
    public Artista criar(@RequestBody Artista artista){
        return service.criar(new Artista(artista.getNome(), artista.getTipo()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artista editar(@PathVariable("id") Long id, @RequestBody Artista artista) {
        return service.editar(id, artista);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
       service.excluir(id);
    }
}
