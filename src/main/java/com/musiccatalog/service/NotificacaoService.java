package com.musiccatalog.service;

import com.musiccatalog.dto.AlbumResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificacaoService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notificarAlbumCriado(AlbumResponse response) {
        messagingTemplate.convertAndSend(
                "/topic/albuns",
                new AlbumCriadoEvent(response.id(), response.nome())
        );
    }

    public record AlbumCriadoEvent(Long id, String nome) {}
}
