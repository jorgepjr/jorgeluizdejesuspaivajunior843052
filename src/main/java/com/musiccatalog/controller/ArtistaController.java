package com.musiccatalog.controller;

import com.musiccatalog.model.Artista;
import com.musiccatalog.service.ArtistaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaController {
    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {this.service = service;}

    @PostMapping
    public Artista criar(@RequestBody Artista artista){
        return service.criar(new Artista(artista.getNome(), artista.getTipo()));
    }
}
