
# Music Catalog API

API REST para gerenciamento de Artistas, Álbuns e Capas, com autenticação JWT,
armazenamento seguro de imagens no MinIO, filtros avançados, health checks, 
WebSocket, rate limiting.

---

## 📦 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.9
- Spring Web, Spring Data JPA
- Spring Security (JWT)
- PostgreSQL
- Flyway (migrações)
- MinIO (compatível com S3)
- Springdoc OpenAPI (Swagger)
- WebSocket (STOMP)
- Docker & Docker Compose
- Maven
- H2 (testes)

---

## 📁 Estrutura do Projeto (resumo)

- controller --> Camada HTTP

- service --> Regras de negócio

- repository --> Persistência

- dto --> Contratos de entrada e saída

- config --> Segurança, WebSocket, Rate Limit

- exception --> Tratamento global de erros

- domain/model --> Entidades JPA


---

## 🔐 Autenticação

A API utiliza JWT com dois tokens:

- Access Token (curta duração - 5 minutos)

- Refresh Token (longa duração - 7 dias)

```
jwt:
expiration: 300000
refresh-expiration: 604800000
````

## 👤 Usuário padrão (seed automático)

- Criado automaticamente na primeira execução do projeto para facilitar testes:
```
username: admin
password: admin
role: ADMIN
______________________
username: aser
password: user
role: USER
```

## 🚀 Como rodar o projeto

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 21 (caso rode localmente)
- Maven (caso rode localmente)

### 1. Gerar o `.jar` da aplicação:

```bash
mvn clean package -DskipTests
```

### 2. Subir os containers:

```bash
docker-compose up --build
```

## Serviços iniciados:

#### API: http://localhost:8080

### Postgres: localhost:5432

### MinIO: http://localhost:9001

### Swagger: http://localhost:8080/swagger-ui.html

---

### 3. Banco de Dados e Pré-cadastros

- As tabelas são criadas automaticamente via Flyway migrations.
- Dados iniciais para testes também são populados (artistas / álbuns).

# 🗄️ MinIO
### Configuração padrão:

```
Endpoint: http://localhost:9000
Access Key: minioadmin
Secret Key: minioadmin123
Bucket: music-catalog
```
### 📌 O bucket é criado automaticamente caso não exista.

- Suporte a múltiplas capas por álbum
- Geração de links pré-assinados
- Expiração padrão: 30 minutos (configurável no ```application.yaml```)

## 📄 Documentação da API (Swagger/OpenAPI)
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs 
```
### Inclui:
- Autenticação Bearer JWT
- Todos os endpoints versionados (/api/v1)
- Schemas de request/response
- Paginação, filtros e ordenação

## 🔔 WebSocket
### Notificação em tempo real para eventos de criação de álbum.

Evento: ```album-criado ```

Endpoints:
```
Handshake: ws://localhost:8080/ws/albums
Tópico: /topic/albuns
```

## 📊 Observabilidade
Health Check: ``` GET /actuator/health ```

Métricas: ``` GET /actuator/metrics ```

## 🚦 Rate Limiting

### Proteção contra abuso de requisições

Retorno padrão:

```
{"error":"Too Many Requests",
  "message":"Você atingiu o limite de 10 requisições por minuto.
 O limite será reiniciado em 41 segundos.": 41
 } 
```

## 📚 Versionamento da API
Todos os endpoints seguem o padrão:
```
/api/v1/**
```

## ✅ Requisitos Implementados

- Segurança (JWT + CORS configurável)
- CRUD completo de Artistas e Álbuns
- Relacionamento N:N Artista ↔ Álbum
- Upload e exclusão de capas
- Paginação e ordenação
- Filtros por nome e tipo de artista (SOLO/BANDA)
- Links pré-assinados para download de imagens
- MinIO (S3-compatible)
- Flyway (schema e dados iniciais)
- Versionamento de API
- Rate limit
- Health check
- WebSocket


