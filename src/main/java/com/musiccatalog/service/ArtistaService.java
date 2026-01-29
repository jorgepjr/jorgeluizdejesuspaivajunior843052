package com.musiccatalog.service;

import com.musiccatalog.exception.RecordNotFoundException;
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

    public Artista editar(Long id, Artista artista){
       return repository.findById(id)
               .map(artistaEncontrado -> {
                   artistaEncontrado.setTipo(artista.getTipo());
                   artistaEncontrado.setNome(artista.getNome());

                   return repository.save(artistaEncontrado);
               }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void excluir(Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }
}
