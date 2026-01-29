package com.musiccatalog.service;

import com.musiccatalog.model.Artista;
import com.musiccatalog.repository.ArtistaRepository;
import org.springframework.stereotype.Service;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;

    public ArtistaService(ArtistaRepository repository) {this.repository = repository;}

    public Artista criar(Artista artista){
        return repository.save(artista);
    }
}
