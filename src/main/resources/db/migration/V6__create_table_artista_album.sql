CREATE TABLE artista_album (
    artista_id BIGINT NOT NULL,
    album_id   BIGINT NOT NULL,

    CONSTRAINT pk_artista_album PRIMARY KEY (artista_id, album_id),

    CONSTRAINT fk_artista_album_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE,

    CONSTRAINT fk_artista_album_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);
