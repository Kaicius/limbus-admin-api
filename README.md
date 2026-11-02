# Limbus Admin API

API REST em **Spring Boot** que gerencia o **catálogo** de um simulador de gacha inspirado em *Limbus Company*: os 12 Sinners, as Identities, os E.G.O. e os banners de extração.

É a API de administração do projeto. O app mobile não a consome: ele conversa com a **Limbus Gacha API**, que lê do mesmo banco o catálogo cadastrado aqui.

---

## Arquitetura do projeto

```
                       ┌──────────────── MySQL ────────────────┐
                       │  sinner · identity · ego · banner      │
                       │  player · player_banner · ...          │
                       └──────▲───────────────────────▲─────────┘
                              │                       │
  limbus-admin-web ──► limbus-admin-api        limbus-gacha-api ◄── app mobile
  (Angular, painel)    (este projeto, :8080)   (:8081, jogadores e sorteio)
         │
         └──► Cloudinary (upload de imagens e vídeos)
```

| Projeto | Papel |
|---|---|
| **limbus-admin-api** | CRUD do catálogo: Identities, E.G.O. e Banners |
| limbus-admin-web | Painel Angular para cadastrar o catálogo com upload de mídia |
| limbus-gacha-api | Login de jogadores, pity, moeda, inventário e sorteio |
| app mobile | Interface do jogador (Expo / React Native) |

As imagens e os vídeos ficam no **Cloudinary**. O painel envia o arquivo direto para lá e a API só guarda a URL.

---

## Tecnologias

- Java 21
- Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- MySQL 8
- Lombok
- springdoc-openapi (Swagger UI)

---

## Modelo de dados

```
Sinner 1 ──── N Identity
Sinner 1 ──── N Ego
Banner N ──── N Identity   (tabela banner_identity)
Banner N ──── N Ego        (tabela banner_ego)
```

| Entidade | Campos principais |
|---|---|
| **Sinner** | `id`, `name` (os 12 são criados automaticamente) |
| **Identity** | `id`, `name`, `rarity` (`ZERO`, `DOUBLE`, `TRIPLE`), `sinner`, `uptie1ArtUrl`, `uptie3ArtUrl`, `uptieCutscene`, `obtainmentVideoUrl` |
| **Ego** | `id`, `name`, `sinner`, `artUrl`, `obtainmentVideoUrl` |
| **Banner** | `id`, `name`, `thumbnailUrl`, `active`, `identities`, `egos` |

As raridades seguem a notação do jogo: `ZERO` = 0, `DOUBLE` = 00, `TRIPLE` = 000.

---

## Como rodar

### Pré-requisitos

- JDK 21
- MySQL 8 rodando (local ou em nuvem, como o Aiven)

### Variáveis de ambiente

A conexão com o banco vem de variáveis de ambiente. O arquivo `.env.exemple` lista todas:

| Variável | Exemplo local | Exemplo Aiven |
|---|---|---|
| `DB_HOST` | `localhost` | `xxx.aivencloud.com` |
| `DB_PORT` | `3306` | `12345` |
| `DB_SSL` | `DISABLED` | `REQUIRED` |
| `DB_USER` | `root` | `avnadmin` |
| `DB_PASSWORD` | `root` | *(senha do serviço)* |

No IntelliJ, configure em *Run → Edit Configurations → Environment variables*.

O banco `limbus` é criado automaticamente na primeira execução (`createDatabaseIfNotExist=true`), e as tabelas são geradas pelo Hibernate (`ddl-auto=update`).

### Executando

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A API sobe em **http://localhost:8080**.

Na primeira execução, o `SinnerSeeder` cadastra os 12 Sinners: Yi Sang, Faust, Don Quixote, Ryōshū, Meursault, Hong Lu, Heathcliff, Ishmael, Rodion, Sinclair, Outis e Gregor.

---

## Documentação interativa (Swagger)

| URL | Conteúdo |
|---|---|
| http://localhost:8080/docs.html | Swagger com o tema Limbus |
| http://localhost:8080/swagger-ui.html | Swagger com o visual padrão |
| http://localhost:8080/v3/api-docs | Especificação OpenAPI em JSON |

Pelo Swagger dá para testar todos os endpoints com o botão **Try it out**.

---

## Endpoints

### Sinners

| Método | Rota | Descrição |
|---|---|---|
| GET | `/sinners` | Lista os 12 Sinners |

### Identities

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/identities` | Cadastra uma identity | 201 |
| GET | `/identities` | Lista todas | 200 |
| GET | `/identities/{id}` | Busca por id | 200 |
| PUT | `/identities/{id}` | Atualiza | 200 |
| DELETE | `/identities/{id}` | Remove | 200 |

### E.G.O.

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/egos` | Cadastra um E.G.O. | 201 |
| GET | `/egos` | Lista todos | 200 |
| GET | `/egos/{id}` | Busca por id | 200 |
| PUT | `/egos/{id}` | Atualiza | 200 |
| DELETE | `/egos/{id}` | Remove | 200 |

### Banners

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/banners` | Cria um banner | 201 |
| GET | `/banners` | Lista todos | 200 |
| GET | `/banners/{id}` | Busca por id, com identities e E.G.O. | 200 |
| PUT | `/banners/{id}` | Atualiza (inclusive a lista de itens) | 200 |
| DELETE | `/banners/{id}` | Remove | 200 |

---

## Exemplos de requisição

### Criar uma Identity

```http
POST /identities
Content-Type: application/json

{
  "name": "LCB Sinner Yi Sang",
  "rarity": "ZERO",
  "sinnerId": 1,
  "uptie1ArtUrl": "https://res.cloudinary.com/.../uptie1.png",
  "uptie3ArtUrl": "https://res.cloudinary.com/.../uptie3.png",
  "uptieCutscene": null,
  "obtainmentVideoUrl": null
}
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "name": "LCB Sinner Yi Sang",
  "rarity": "ZERO",
  "sinnerId": 1,
  "sinnerName": "Yi Sang",
  "uptie1ArtUrl": "https://res.cloudinary.com/.../uptie1.png",
  "uptie3ArtUrl": "https://res.cloudinary.com/.../uptie3.png",
  "uptieCutscene": null,
  "obtainmentVideoUrl": null
}
```

### Criar um E.G.O.

```http
POST /egos
Content-Type: application/json

{
  "name": "Crow's Eye View",
  "sinnerId": 1,
  "artUrl": "https://res.cloudinary.com/.../ego.png",
  "obtainmentVideoUrl": "https://res.cloudinary.com/.../ego.mp4"
}
```

### Criar um Banner

```http
POST /banners
Content-Type: application/json

{
  "name": "Extração Padrão",
  "thumbnailUrl": "https://res.cloudinary.com/.../banner.png",
  "active": true,
  "identityIds": [1, 2, 3, 13, 14, 15, 16],
  "egoIds": [1]
}
```

A resposta traz as identities completas, ordenadas por raridade e depois por nome, e os E.G.O. do banner.

---

## Regras de validação

**Identity**
- `name` é obrigatório (até 100 caracteres).
- `rarity` é obrigatório: `ZERO`, `DOUBLE` ou `TRIPLE`.
- `sinnerId` é obrigatório e precisa existir.

**E.G.O.**
- `name` é obrigatório (até 100 caracteres).
- `sinnerId` é obrigatório e precisa existir.

**Banner**
- `name` é obrigatório (até 100 caracteres).
- `identityIds` precisa ter pelo menos 1 id, e todos precisam existir.
- O banner precisa ter **pelo menos uma identity de cada raridade** (0, 00 e 000). Sem isso, o sorteio da gacha-api não teria o que entregar em alguma faixa de raridade.
- `egoIds` é opcional. Um banner sem E.G.O. usa a tabela de taxas sem E.G.O. na gacha-api.
- `active` é opcional e vale `true` quando omitido.

**Exclusões**
- Uma identity ou um E.G.O. que esteja em algum banner não pode ser removido.
- Um banner em que algum jogador já girou também não pode ser removido. Para tirá-lo do ar, envie um `PUT` com `"active": false`.

---

## Erros

Todas as respostas de erro têm o mesmo formato:

```json
{ "error": "mensagem explicando o problema" }
```

| Status | Quando acontece |
|---|---|
| 400 Bad Request | Campo inválido ou faltando, ou banner sem as 3 raridades |
| 404 Not Found | Id de identity, E.G.O., sinner ou banner inexistente |
| 409 Conflict | Tentativa de remover algo que está em uso |

Exemplo de `400`:

```json
{ "error": "name: must not be blank; sinnerId: must not be null" }
```

---

## Estrutura do código

```
src/main/java/com/kaio/limbus_admin_api
 ├─ config        CORS, carga inicial dos Sinners e configuração do OpenAPI
 ├─ controller    endpoints REST
 ├─ dto           records de request e response
 ├─ entity        entidades JPA
 ├─ enums         Rarity
 ├─ expection     exceções e tratamento global de erros
 ├─ repository    interfaces Spring Data JPA
 └─ service       regras de negócio

src/main/resources
 ├─ application.properties
 └─ static        docs.html e swagger-limbus.css (Swagger com o tema Limbus)
```

---

## CORS

As origens liberadas ficam em `config/CorsConfig.java`. Por padrão, apenas `http://localhost:4200` (o painel Angular) pode chamar a API pelo navegador.

---

*Projeto acadêmico sem fins comerciais. Limbus Company e seus personagens pertencem à Project Moon.*
