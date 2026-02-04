package com.musiccatalog.service;

import com.musiccatalog.dto.CapaAlbumResponse;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Album;
import com.musiccatalog.model.Capa;
import com.musiccatalog.repository.AlbumRepository;
import com.musiccatalog.repository.CapaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class CapaService {

    private final CapaRepository capaRepository;
    private final AlbumRepository albumRepository;
    private final MinioService storageService;

    public CapaService(CapaRepository repository, AlbumRepository albumRepository, MinioService storageService) {
        this.capaRepository = repository;
        this.albumRepository = albumRepository;
        this.storageService = storageService;
    }

    public Capa salvar(Long albumId, MultipartFile arquivo) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RecordNotFoundException(albumId));

        String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();

        storageService.upload(arquivo, nomeArquivo);

        return capaRepository.save(new Capa(album, nomeArquivo));
    }

    public CapaAlbumResponse obterCapa(Long albumId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album não encontrado"));

        List<Capa> capas = capaRepository.findByAlbumId(albumId);

        List<String> links = capas == null ? Collections.emptyList() :
                capas.stream()
                        .map(Capa::getArquivoHash)
                        .filter(Objects::nonNull)
                        .map(hash -> {
                            try {
                                return storageService.gerarLinkTemporario(hash);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

        return new CapaAlbumResponse(
                album.getId(),
                album.getNome(),
                links
        );
    }

    public void excluir(Long id) {
        capaRepository.delete(capaRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }
}
