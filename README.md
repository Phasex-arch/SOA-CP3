# Auto Escola 3ESA - API REST

API REST desenvolvida com Spring Boot para gerenciamento de uma auto-escola, incluindo cadastro de instrutores e alunos.

**Disciplina:** SOA e WebServices  
**Professor:** Carlos Eduardo Machado de Oliveira  
**Checkpoint 3**

## Integrantes do Grupo

| Nome              | RM       |
|-------------------|----------|
| Ricardo Fernandes | RM554597 |
| Khadija Lima      | RM668971 |
| Isadora Meneghetti           | RM556326 |
| Henrique Azevedo          | RM556707 |
| Gustavo Jun       | RM554718 |

## Tecnologias

- Java 25
- Spring Boot 4.0.5
- Spring Data JPA
- Flyway (migrações de banco)
- Bean Validation
- Lombok
- MySQL

## Como Rodar

1. Ter o MySQL rodando na porta **3306** com um banco chamado `autoescola3esa`:
   ```sql
   CREATE DATABASE autoescola3esa;
   ```
   1. Se não tiver, rode o docker compose:
   ```bash
   docker compose up -d
   ```
2. Conferir as credenciais em `src/main/resources/application.properties` (padrão: `root` / `fiap`).
3. Executar o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API sobe na porta **8085**.

## Endpoints

### Health Check

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | `/health_check` | Verificacao de integridade da API |

### Instrutores

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/instrutores` | Cadastrar instrutor |
| GET | `/instrutores` | Listar instrutores (paginado, 10/pagina, ordenado por nome) |
| GET | `/instrutores/{id}` | Detalhar instrutor por ID |
| PUT | `/instrutores` | Atualizar instrutor |
| DELETE | `/instrutores/{id}` | Excluir instrutor (soft delete) |

### Alunos

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/alunos` | Cadastrar aluno |
| GET | `/alunos` | Listar alunos (paginado, 10/pagina, ordenado por nome) |
| GET | `/alunos/{id}` | Detalhar aluno por ID |
| PUT | `/alunos` | Atualizar aluno |
| DELETE | `/alunos/{id}` | Excluir aluno (soft delete) |

