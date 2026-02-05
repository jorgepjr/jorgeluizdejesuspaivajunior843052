package com.musiccatalog.service;

import com.musiccatalog.dto.ArtistaResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Artista;
import com.musiccatalog.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;

    public ArtistaService(ArtistaRepository repository) {this.repository = repository;}

    public Artista criar(Artista artista) {
        return repository.save(artista);
    }

    public Artista editar(Long id, Artista artista) {
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

    public Page<ArtistaResponse> filtrar(String nome, TipoArtista tipo, Pageable pageable) {
        Page<Artista> page;

        if (nome == null && tipo == null) {
            page = repository.findAll(pageable);
        } else {
            page = repository.filtrar(nome, tipo, pageable);
        }

        return page.map(a -> new ArtistaResponse(a.getId(), a.getNome(), a.getTipo()));

    }
}
