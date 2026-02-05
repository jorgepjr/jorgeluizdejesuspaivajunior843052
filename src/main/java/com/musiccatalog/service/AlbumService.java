package com.musiccatalog.service;

import com.musiccatalog.dto.AlbumResponse;
import com.musiccatalog.dto.ArtistaResponse;
import com.musiccatalog.dto.CapaResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Album;
import com.musiccatalog.model.Artista;
import com.musiccatalog.model.ArtistaAlbum;
import com.musiccatalog.model.ArtistaAlbumId;
import com.musiccatalog.repository.AlbumRepository;
import com.musiccatalog.repository.ArtistaAlbumRepository;
import com.musiccatalog.repository.ArtistaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final ArtistaAlbumRepository artistaAlbumRepository;
    private final MinioService minioService;


    public AlbumService(AlbumRepository albumRepository, ArtistaRepository artistaRepository, ArtistaAlbumRepository artistaAlbumRepository, MinioService minioService) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.artistaAlbumRepository = artistaAlbumRepository;
        this.minioService = minioService;
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

    @Transactional
    public Page<AlbumResponse> filtrar(String nome, TipoArtista tipoArtista, Pageable pageable) {
        Page<Album> page;

        if (nome == null && tipoArtista == null) {
            page = albumRepository.findAll(pageable);
        } else {
            page = albumRepository.filtrar(nome, tipoArtista, pageable);
        }

        return page.map(album -> new AlbumResponse(
                album.getId(),
                album.getNome(),
                album.getArtistas().stream()
                        .map(a -> new ArtistaResponse(a.getArtista().getId(), a.getArtista().getNome(), a.getArtista().getTipo()))
                        .toList(),
                album.getCapas().stream()
                        .map(c -> {
                            try {
                                return new CapaResponse(c.getId(), minioService.gerarLinkTemporario(c.getArquivoHash()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList()
        ));
    }

}
