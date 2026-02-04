package com.musiccatalog.service;

import com.musiccatalog.dto.AlbumResponse;
import com.musiccatalog.dto.PagedResponse;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Album;
import com.musiccatalog.model.Artista;
import com.musiccatalog.model.ArtistaAlbum;
import com.musiccatalog.model.ArtistaAlbumId;
import com.musiccatalog.repository.AlbumRepository;
import com.musiccatalog.repository.ArtistaAlbumRepository;
import com.musiccatalog.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final ArtistaAlbumRepository artistaAlbumRepository;


    public AlbumService(AlbumRepository albumRepository, ArtistaRepository artistaRepository, ArtistaAlbumRepository artistaAlbumRepository) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.artistaAlbumRepository = artistaAlbumRepository;
    }

    public Album criar(Album album) {
        return albumRepository.save(album);
    }

    public Album editar(Long id, Album album) {
        return albumRepository.findById(id)
                .map(albumEncontrado -> {
                    albumEncontrado.setNome(album.getNome());

                    return albumRepository.save(albumEncontrado);
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void excluir(Long id) {
        albumRepository.delete(albumRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public PagedResponse<AlbumResponse> obterPaginado(String nome, Pageable pageable) {
        Page<Album> page;

        if (nome != null && !nome.isBlank()) {
            page = albumRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = albumRepository.findAll(pageable);
        }

        List<AlbumResponse> albuns = page.stream()
                .map(x -> new AlbumResponse(x.getId(), x.getNome()))
                .toList();

        return new PagedResponse<>(
                albuns,
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public void vincularArtista(Long albumId, Long artistaId) {
        Artista artista = artistaRepository.findById(artistaId)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album não encontrado"));

        ArtistaAlbumId id = new ArtistaAlbumId(artistaId, albumId);

        if (artistaAlbumRepository.existsById(id)) {
            throw new RuntimeException("Artista já está vinculado ao álbum");
        }

        ArtistaAlbum rel = new ArtistaAlbum(artista, album);

        artistaAlbumRepository.save(rel);
    }

    public void desvincularArtista(Long artistaId, Long albumId) {
        ArtistaAlbumId id = new ArtistaAlbumId(artistaId, albumId);

        if (!artistaAlbumRepository.existsById(id)) {
            throw new RuntimeException("Vinculo nao existe");
        }

        artistaAlbumRepository.deleteById(id);
    }

    public Album obterPorId(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}
