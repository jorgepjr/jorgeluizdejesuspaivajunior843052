package com.musiccatalog.service;

import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Album;
import com.musiccatalog.model.Capa;
import com.musiccatalog.repository.AlbumRepository;
import com.musiccatalog.repository.CapaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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

    public List<Capa> findByAlbumId(Long albumId) {
        return capaRepository.findByAlbumId(albumId);
    }

    public String obterCapa(String arquivoHash) throws Exception {
        return storageService.gerarLinkTemporario(arquivoHash);
    }
}
