# Calculator API (REST + Kafka + Docker)

## 1. Objetivo do Projeto
O **Calculator API** é uma aplicação **multi-módulo** em Java com **Spring Boot**, dividida em dois serviços independentes:

- **REST Service (`rest`)**  
  Expõe uma API HTTP com operações matemáticas (soma, subtração, multiplicação, divisão).  
  Valida inputs, gere erros e envia mensagens ao serviço `calculator` via Kafka.

- **Calculator Service (`calculator`)**  
  Consome pedidos via Kafka, executa operações com `BigDecimal` de alta precisão e devolve resultados ao `rest`.

Comunicação assíncrona entre os módulos é feita com **Apache Kafka** (modo KRaft), totalmente orquestrado em **Docker Compose**.

---

## 2. Arquitetura
```
[REST Service]  <--HTTP-->  Cliente
     |
     v
   [Kafka]  <----mensagens---->
     ^
     |
[Calculator Service]
```

- **API Gateway**: REST em `localhost:8080`
- **Core Engine**: Calculator em `localhost:8081`
- **Mensageria**: Kafka em `localhost:9092`

---

## 3. Tecnologias Utilizadas
- Java 17 (Eclipse Temurin)
- Spring Boot 3.x
  - Spring Web
  - Spring Validation
  - Spring Kafka
  - Spring Actuator
- Maven (multi-módulo)
- Apache Kafka 3.7.0 (KRaft mode)
- Docker & Docker Compose
- Lombok
- Jackson Databind
- Swagger/OpenAPI
- JUnit 5 & Mockito para testes

---

## 4. Como Correr o Projeto

### 4.1. Pré-requisitos
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- Git configurado

### 4.2. Clonar e compilar
```bash
git clone https://github.com/pedropires64/calculatorwit.git
cd calculator-api
mvn clean install -DskipTests
```

### 4.3. Subir em Docker
```bash
docker compose -f docker-compose.yml -f docker-compose.app.yml up -d --build
```

Ver containers:
```bash
docker compose ps
```

---

## 5. Endpoints Principais

### Health check
```bash
curl -s http://localhost:8080/actuator/health
curl -s http://localhost:8081/actuator/health
# Esperado: {"status":"UP"}
```

### Operações
```bash
# Soma
curl -s -X POST http://localhost:8080/api/v1/calc/sum   -H 'Content-Type: application/json'   -d '{"a":"1.234567890123456789","b":"2"}'

# Divisão
curl -s -X POST http://localhost:8080/api/v1/calc/div   -H 'Content-Type: application/json'   -d '{"a":"10","b":"3"}'

# Divisão por zero
curl -s -X POST http://localhost:8080/api/v1/calc/div   -H 'Content-Type: application/json'   -d '{"a":"10","b":"0"}'
```

### Swagger
Aceder em [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 6. Testes

### Unitários e Integração
```bash
mvn test
```

- `calculator`: garante precisão com `BigDecimal` e valida operações.
- `rest`: valida API, formatação de mensagens, e tratamento de erros (`Division by zero`, `Invalid number format`).

---

## 7. Exemplos de Respostas

**Soma**
```json
{
  "operation": "sum",
  "a": "1.234567890123456789",
  "b": "2",
  "result": "3.234567890123456789",
  "requestId": "uuid"
}
```

**Erro (divisão por zero)**
```json
{
  "message": "Division by zero"
}
```

**Erro (formato inválido)**
```json
{
  "message": "Invalid number format"
}
```

---

## 8. Dificuldades e Soluções

#### 8.1. Maven e Dependências
- **Erro**: `cannot access com.fasterxml.jackson.core.type.TypeReference`
- **Solução**: adicionada dependência explícita `jackson-databind` no `pom.xml` para resolver problemas com `TypeReference`.

#### 8.2. Encoding em `application.properties`
- **Erro**: `MalformedInputException: Input length = 1`
- **Solução**: recriação do ficheiro em UTF-8 e configuração do `maven-resources-plugin` com `filtering=false` para evitar substituições incorretas.

#### 8.3. Kafka no Docker
- **Erro**: `bitnami/kafka:3.7 not found` e `invalid url in bootstrap.servers: localhost: 9092`
- **Solução**: substituição da imagem por `apache/kafka:3.7.0` em modo KRaft e ajuste correto dos `KAFKA_ADVERTISED_LISTENERS` (interno/externo).

#### 8.4. Timeouts REST ↔ Calculator
- **Erro**: chamadas `curl` retornavam `{"message":"Upstream timeout"}`
- **Solução**: ajuste do `docker-compose` para incluir `depends_on` com `healthcheck`, garantindo que Kafka estivesse `UP` antes do arranque das aplicações.

#### 8.5. Testes
- **Erro**: dependências `JUnit`/`SpringBootTest` não encontradas (`package org.junit.jupiter.api does not exist`).
- **Solução**: adição e correção das dependências `spring-boot-starter-test` e `spring-kafka-test` no `pom.xml` com escopo `test`.

#### 8.6. Portas ocupadas
- **Erro**: `Address already in use` ao arrancar serviços em 8080/8081.
- **Solução**: identificação de processos com `lsof -nP -iTCP:8080 -sTCP:LISTEN` e encerramento antes de reiniciar os serviços.

#### 8.7. Execução Maven vs Docker
- **Erro**: REST subia em Maven mas o Calculator falhava, ou vice-versa.
- **Solução**: padronização em dois modos de execução documentados — **modo Dev (Maven)** e **modo Prod (Docker)**.

#### 8.8. Git e Autenticação
- **Erro**: `Permission denied (publickey)` ao fazer `git push`.
- **Solução**: reconfiguração para HTTPS em vez de SSH e adoção de convenções de commits semânticos (`feat:`, `fix:`, `chore:`).

---

## 9. Próximos Passos (Extras Opcionais)
- Documentação finalizada no README
- Adicionar **coleção Postman** para fácil importação.
- Criar **pipeline CI/CD** (GitHub Actions).
- Criar **tag v1.0.0** e release oficial.

---

## 10. Licença
WIT © 2025 Pedro Pires
