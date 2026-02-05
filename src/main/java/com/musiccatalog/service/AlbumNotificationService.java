package com.musiccatalog.service;

import com.musiccatalog.dto.AlbumResponse;
import com.musiccatalog.model.Album;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlbumNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public AlbumNotificationService(SimpMessagingTemplate messagingTemplate) {this.messagingTemplate = messagingTemplate;}

    public void notifyAlbumCreated(AlbumResponse response) {
        messagingTemplate.convertAndSend(
                "/albuns",
                new AlbumCreatedEvent(response.id(), response.nome())
        );
    }

    public record AlbumCreatedEvent(Long id, String nome) {}
}
