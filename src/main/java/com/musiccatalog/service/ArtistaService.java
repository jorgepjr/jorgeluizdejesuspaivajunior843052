package com.musiccatalog.service;

import com.musiccatalog.dto.PagedResponse;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Artista;
import com.musiccatalog.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Artista obterPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public PagedResponse<Artista> obterPaginado(int pageNumber, int pageSize) {
        Page<Artista> pageArtistas = repository.findAll(PageRequest.of(pageNumber, pageSize));
        List<Artista> artistas = pageArtistas.getContent();
        return new PagedResponse<>(artistas, pageArtistas.getTotalElements(), pageArtistas.getTotalPages());
    }
}
