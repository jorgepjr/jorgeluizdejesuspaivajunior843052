
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

## 🔑 Como Gerar e Usar o Token JWT
Para acessar os endpoints protegidos, 
é necessário gerar um Access Token via login e utilizá-lo nos requests (Swagger ou Postman).

### 1. Login para gerar token
- Endpoint: POST  ``` /api/v1/auth/login ```

Exemplo de request:

```
{
"username": "admin",
"password": "admin"
}'
```
Response: 
```
{
"accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
"refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
### 2. Usar token nos endpoints protegidos

No Swagger ou Postman, adicione o Access Token no header:

``` Authorization: Bearer {accessToken} ```



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

## 🌐 Regionais (Sync)
### Sincronização automática com API externa

- Endpoint: ```POST /api/v1/regionais/sync```
- Faz download da lista de regionais da API externa e atualiza o banco local.
- Cria novos registros se não existirem, atualiza os existentes e mantém ativo/sincId corretamente.

Resposta da requisicao: 
```
[
  {
    "id": 9,
    "nome": "REGIONAL DE CUIABÁ",
    "ativo": true,
    "sincId": "9",
    "createdAt": "2026-02-04T18:00:00",
    "updatedAt": "2026-02-04T18:05:00"
  },
  {
    "id": 31,
    "nome": "REGIONAL DE GUARANTÃ DO NORTE",
    "ativo": true,
    "sincId": "31",
    "createdAt": "2026-02-04T18:00:00",
    "updatedAt": "2026-02-04T18:05:00"
  }
]
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
- Swagger (Documentacao Basica)
- Sincronizacao de Regionais


