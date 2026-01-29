package com.musiccatalog.service;

import com.musiccatalog.dto.PagedResponse;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Album;
import com.musiccatalog.repository.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository repository;

    public AlbumService(AlbumRepository repository) {this.repository = repository;}

    public Album criar(Album album){
        return repository.save(album);
    }

    public Album editar(Long id, Album album){
       return repository.findById(id)
               .map(albumEncontrado -> {
                   albumEncontrado.setNome(album.getNome());

                   return repository.save(albumEncontrado);
               }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void excluir(Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public Album obterPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public PagedResponse<Album> obterPaginado(int pageNumber, int pageSize) {
        Page<Album> pageAlbum = repository.findAll(PageRequest.of(pageNumber, pageSize));
        List<Album> albuns = pageAlbum.getContent();
        return new PagedResponse<>(albuns, pageAlbum.getTotalElements(), pageAlbum.getTotalPages());
    }
}
