# Auto Escola 3ESA - API REST

API REST desenvolvida com Spring Boot para gerenciamento de uma auto-escola: cadastro de instrutores e alunos, agendamento e cancelamento de instrucoes, com autenticacao JWT e controle de acesso por perfil.

**Disciplina:** SOA e WebServices  
**Professor:** Carlos Eduardo Machado de Oliveira  
**Checkpoint 4**

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
- Spring Security + JWT (java-jwt)
- BCrypt (encriptacao de senhas)
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

O segredo do token JWT vem da variavel de ambiente `JWT_SECRET` (ha um valor padrao para desenvolvimento em `application.properties`).

## Autenticacao

Todas as rotas exigem um token JWT, exceto `POST /login` e `GET /health_check`.

O Flyway ja cria um usuario administrador:

| Login | Senha | Perfil |
|-------|-------|--------|
| `admin@autoescola.com` | `123456` | ADMIN |

Para obter o token:

```bash
curl -X POST http://localhost:8085/login \
  -H 'Content-Type: application/json' \
  -d '{"login":"admin@autoescola.com","senha":"123456"}'
```

E enviar nas demais requisicoes o header `Authorization: Bearer <token>`.

As senhas sao gravadas no banco com hash BCrypt, nunca em texto puro.

## Endpoints

### Health Check

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | `/health_check` | Verificacao de integridade da API (publico) |

### Autenticacao

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/login` | Autenticar e receber o token JWT (publico) |

### Usuarios

| Metodo | Rota | Descricao | Acesso |
|--------|------|-----------|--------|
| POST | `/usuarios` | Cadastrar usuario (senha encriptada com BCrypt) | ADMIN |
| GET | `/usuarios` | Listar usuarios (paginado, 10/pagina) | ADMIN |
| PUT | `/usuarios` | Atualizar o perfil de um usuario | ADMIN |
| DELETE | `/usuarios/{id}` | Excluir usuario | ADMIN |
| PUT | `/usuarios/senha` | Alterar a propria senha (exige a senha atual) | Autenticado |

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

### Instrucoes

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/instrucoes` | Agendar instrucao |
| DELETE | `/instrucoes` | Cancelar instrucao (informando o motivo) |
| GET | `/instrucoes` | Listar instrucoes ativas (paginado, 10/pagina) |
| GET | `/instrucoes/{id}` | Detalhar instrucao por ID |

Agendar (o campo `instrutorId` e opcional; se omitido, o sistema sorteia um instrutor livre no horario):

```json
POST /instrucoes
{ "alunoId": 1, "instrutorId": 2, "data": "2026-09-15 10:00" }
```

Cancelar (motivo: `ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU` ou `OUTROS`):

```json
DELETE /instrucoes
{ "id": 1, "motivo": "ALUNO_DESISTIU" }
```

#### Regras de negocio

**Agendamento**

- Funcionamento de segunda a sabado, das 06:00 as 21:00 (instrucao de 1 hora, ultimo inicio as 20:00, sempre em hora cheia);
- Antecedencia minima de 30 minutos;
- Aluno e instrutor precisam estar ativos;
- No maximo 2 instrucoes por dia para o mesmo aluno;
- Um instrutor nao pode ter duas instrucoes na mesma data/hora;
- Instrutor opcional: sem ele, o sistema escolhe aleatoriamente um instrutor livre.

**Cancelamento**

- Motivo obrigatorio (`ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU`, `OUTROS`);
- Antecedencia minima de 24 horas;
- O cancelamento nao apaga a instrucao, apenas registra o motivo.

## Testes

```bash
./mvnw test
```

`InstrucaoServiceTest` cobre as regras de horario e antecedencia de agendamento/cancelamento.

