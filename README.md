# Project - Bookstore: API REST com Spring Boot

API RESTful desenvolvida em Java e Spring Boot para gerenciar Autores e Livros, como parte do Checkpoint 2 da FIAP.

## Recursos e Requisitos Implementados

-   ✅ **Projeto Compilável:** O projeto é construído com Maven (`mvn clean package`).
-   ✅ **Endpoints REST:** CRUD completo para Autores e Livros.
-   ✅ **Console H2:** Banco de dados em memória acessível via navegador (`/h2-console`).
-   ✅ **Tratamento de Exceções:** `ResourceNotFoundException` customizada e tratada globalmente com `@ControllerAdvice`.
-   ✅ **Arquitetura Limpa:**
    -   DTOs implementados como `record`.
    -   Camada de `Service` exposta por interfaces.
    -   `Mapper` para conversão entre DTOs e Entidades.
-   ✅ **Testes:**
    -   Cobertura de código via Jacoco `>= 50%`.
    -   Pelo menos 1 teste de integração que valida um endpoint.

## Pré-requisitos

-   Java 17 ou superior
-   Apache Maven 3.8+

## Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/k-auaferreira/bookstore-rest-api.git
    cd bookstore-rest-api
    ```

2.  **Execute a aplicação:**

    ```bash
    mvn spring-boot:run
    ```

A API estará rodando em `http://localhost:8080`.

## Endpoints da API

### Autores (`/authors`)

| Método   | Endpoint              | Descrição                      |
| :------- | :-------------------- | :----------------------------- |
| `POST`   | `/authors`            | Cria um novo autor.           |
| `GET`    | `/authors`            | Lista todos os autores.       |
| `GET`    | `/authors/{id}`       | Busca um autor por ID.        |
| `PUT`    | `/authors/{id}`       | Atualiza um autor.            |
| `DELETE` | `/authors/{id}`       | Remove um autor.              |
| `GET`    | `/authors/{id}/books` | Lista os livros de um autor. |

### Livros (`/books`)

| Método   | Endpoint      | Descrição                |
| :------- | :------------ | :----------------------- |
| `POST`   | `/books`      | Cria um novo livro.     |
| `GET`    | `/books`      | Lista todos os livros.  |
| `GET`    | `/books/{id}` | Busca um livro por ID. |
| `PUT`    | `/books/{id}` | Atualiza um livro.      |
| `DELETE` | `/books/{id}` | Remove um livro.        |

## Banco de Dados H2

-   **Acessível em:** `http://localhost:8080/h2-console`
-   **JDBC URL:** `jdbc:h2:mem:bookstoredb`
-   **User Name:** `sa`
-   **Password:** `password`

## Testes e Relatório de Cobertura

-   **Para rodar os testes:**
    ```bash
    mvn clean test
    ```
-   **O relatório de cobertura do Jacoco está disponível em:** `target/site/jacoco/index.html`.

## Repositório no GitHub

[https://github.com/k-auaferreira/bookstore-rest-api](https://github.com/k-auaferreira/bookstore-rest-api)