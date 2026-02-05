
# Music Catalog API

API REST para gerenciamento de Artistas, Álbuns e Capas, com autenticação JWT,
armazenamento seguro de imagens no MinIO, filtros avançados, health checks, 
WebSocket, rate limiting e sincronização de dados regionais.

---

## 📦 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- MinIO
- Docker & Docker Compose
- Maven

---

## 📁 Estrutura do Projeto

```
music-catalog-api/
├── src/
│   ├── main/java/com/music/catalog/
│   │   ├── MusicCatalogApiApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── CorsConfig.java
│   │   │   ├── MinioConfig.java
│   │   │   ├── WebSocketConfig.java
│   │   │   ├── RateLimitConfig.java
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── security/
│   │   ├── exception/
│   │   ├── util/
│   │   └── health/
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-docker.yml
│       └── db/migration/
│
├── src/test/java/com/music/catalog/
│   ├── controller/
│   ├── service/
│   └── integration/
│
├── docker/
│   ├── Dockerfile
│   └── Dockerfile.prod
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🚀 Como Rodar com Docker

### 1. Gerar o `.jar` da aplicação:

```bash
mvn clean package -DskipTests
```

### 2. Subir os containers:

```bash
docker-compose up --build
```

A aplicação estará disponível em:  
📍 `http://localhost:8080`

---

## ✅ Requisitos

- Docker e Docker Compose instalados
- Java 21 (caso rode localmente)
- Maven (caso rode localmente)

## 📖 Documentação da API

### Swagger/OpenAPI
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```
## ✅ Requisitos Implementados

### Requisitos Gerais ✓

- [x] **Segurança (CORS)**: Bloqueio de acesso por domínio (configurável)
- [x] **JWT**: Autenticação com expiração de 5 minutos e refresh token com validade de 7 dias
- [x] **Operações CRUD**: POST, PUT, GET (DELETE também implementado)
- [x] **Paginação**: Listagem de álbuns com Page e Sort
- [x] **Consultas Parametrizadas**: Filtros por tipo de artista (SOLO/BANDA)
- [x] **Busca por Artista**: Filtro por nome com ordenação (ASC/DESC)
- [x] **Upload de Imagens**: Múltiplas capas por álbum
- [x] **MinIO S3**: Armazenamento seguro de imagens
- [x] **Links Pré-assinados**: Expiração de 30 minutos (configurável)
- [x] **Versionamento**: API em `/api/v1/`
- [x] **Flyway Migrations**: Schema e dados iniciais


