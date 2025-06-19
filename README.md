# Indora API

## Descrição
A Indora API é uma aplicação backend desenvolvida em Java com Spring Boot, responsável por gerenciar autenticação, usuários e permissões, integrando-se ao banco de dados PostgreSQL. O projeto utiliza autenticação via OAuth2 com GitHub e segue boas práticas de arquitetura REST.

## Funcionalidades
- Cadastro e autenticação de usuários
- Integração com OAuth2 (GitHub)
- Gerenciamento de permissões e papéis (roles)
- Registro de logs e tratamento de exceções
- Migrações de banco de dados com Flyway

## Tecnologias Utilizadas
- Java 17+
- Spring Boot 3+
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker & Docker Compose

## Estrutura do Projeto
- `src/main/java/com/fiap/indora/` - Código-fonte principal
  - `controller/` - Controllers REST
  - `dtos/` - Data Transfer Objects
  - `model/` - Entidades JPA
  - `repositories/` - Repositórios JPA
  - `services/` - Lógica de negócio
  - `configs/` - Configurações gerais e de segurança
- `src/main/resources/` - Recursos da aplicação
  - `application.yml` - Configurações do Spring Boot
  - `db/migration/` - Scripts de migração Flyway

## Como executar localmente
1. **Pré-requisitos:**
   - Docker e Docker Compose instalados
   - Java 17+ e Maven (para rodar localmente sem Docker)

2. **Configuração de variáveis de ambiente:**
   - Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
     ```env
     AUTH_GIT_HUB_CLIENTID=seu_client_id
     AUTH_GIT_HUB_CLIENTSECRET=seu_client_secret
     AUTH_GIT_HUB_REDIRECTURI=sua_redirect_uri
     ```

3. **Subindo com Docker Compose:**
   ```sh
   docker-compose up -d
   ```
   Isso irá subir o banco de dados PostgreSQL e a API.

4. **Acessando a aplicação:**
   - A API estará disponível em: `http://localhost:8080`

## Migrações de Banco de Dados
As migrações são gerenciadas automaticamente pelo Flyway ao iniciar a aplicação. Os scripts estão em `src/main/resources/db/migration/`.

## Testes
Para rodar os testes automatizados:
```sh
./mvnw test
```

## Contato
Dúvidas ou sugestões? Entre em contato com a equipe de desenvolvimento.

