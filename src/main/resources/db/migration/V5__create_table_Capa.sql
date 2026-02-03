CREATE TABLE capa (
    id BIGSERIAL PRIMARY KEY,
    album_id BIGINT NOT NULL,
    arquivo_hash VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT capa FOREIGN KEY (album_id) REFERENCES album(id)
);
