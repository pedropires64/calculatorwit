# Calculator API — Fase 1

Projeto **greenfield** para uma API de cálculo com arquitetura em **dois serviços**:
- **rest**: expõe endpoints HTTP (sum/sub/mul/div) e orquestra pedidos.
- **calculator**: serviço assíncrono que processa operações via **Kafka** e devolve o resultado.

> Este README é a versão inicial (Fase 1). No fim do desenvolvimento será atualizado com instruções completas de setup, execução e documentação da API.

## Objetivos (roadmap inicial)
- [ ] **F1**: Setup de repositório, README e convenções de commits/branches.
- [ ] **F2**: Criar projeto Maven multi-módulo (`parent`, `rest`, `calculator`) com Spring Boot 3.
- [ ] **F3**: Subir Kafka via Docker Compose.
- [ ] **F4**: Implementar operações básicas (sum/sub/mul/div) com `BigDecimal` (precisão configurável).
- [ ] **F5**: Fluxo request/reply via Kafka entre `rest` ↔ `calculator`.
- [ ] **F6**: Tests unitários e integração (mínimos).
- [ ] **F7**: Endpoints REST com validação e tratamento de erros.
- [ ] **F8**: Documentação final (README completo + exemplos `curl`).

## Stack (prevista)
- **Java 17**, **Spring Boot 3.5.x**
- **Apache Kafka** (Docker)
- **Maven**
- Testes: JUnit 5, Mockito (a definir)

## Convenções (propostas)
- **Branches**: `feat/<tarefa>`, `fix/<issue>`, `chore/<tema>`.
- **Commits** (conventional-ish):
  - `feat(rest): cria endpoint /sum`
  - `fix(calculator): corrige deserialização JSON`
  - `chore(ci): adiciona workflow de build`
- **PRs** curtos por tarefa, com descrição clara.

## Como correr (a definir mais tarde)
Instruções completas serão adicionadas na **Fase Final**:
- instalação de JDK 17
- `docker compose up -d` para Kafka
- `mvn clean install` e execução dos serviços
- exemplos `curl` para testar

---

© 2025 — Projeto WIT
