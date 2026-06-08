# Bank Transfer API

API REST desenvolvida em Java/Spring Boot para simular operações básicas de um banco digital.

## Funcionalidades

* Cadastro de contas
* Consulta de contas
* Transferência entre contas
* Consulta de movimentações financeiras
* Registro de movimentações
* Notificação após transferência
* Tratamento de erros de negócio

## Tecnologias

* Java 21
* Spring Boot 3
* Spring Data JPA
* H2 Database
* Lombok
* Swagger/OpenAPI
* JUnit 5
* Mockito

## Arquitetura

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
H2 Database
```

## Regras de Negócio

* Conta origem deve existir
* Conta destino deve existir
* Conta origem deve possuir saldo suficiente
* Transferência executada de forma transacional (`@Transactional`)
* Movimentação registrada após sucesso
* Notificação enviada após sucesso
* Controle de concorrência através de Optimistic Locking (`@Version`)

## Executando o Projeto

### Pré-requisitos

* Java 21+
* Maven 3.9+

### Executar aplicação

```bash
mvn spring-boot:run
```

Aplicação disponível em:

```text
http://localhost:8080
```

## Documentação da API

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Banco de Dados

Console H2:

```text
http://localhost:8080/h2-console
```

Configurações padrão:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:
```

## Executar Testes

```bash
mvn test
```

## Endpoints

### Criar Conta

**POST** `/accounts`

Request:

```json
{
  "name": "John",
  "initialBalance": 1000.00
}
```

Response:

```json
{
  "id": 1,
  "name": "John",
  "balance": 1000.00
}
```

---

### Listar Contas

**GET** `/accounts`

Response:

```json
[
  {
    "id": 1,
    "name": "John",
    "balance": 1000.00
  }
]
```

---

### Transferir Valores

**POST** `/transfers`

Request:

```json
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 100.00
}
```

Response:

```json
{
  "message": "Transfer completed successfully"
}
```

---

### Listar Todas as Movimentações

**GET** `/transactions`

Response:

```json
[
  {
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "amount": 100.00,
    "createdAt": "2026-06-07T15:00:00"
  }
]
```

---

### Consultar Movimentações de uma Conta

**GET** `/transactions/account/{accountId}`

Exemplo:

```http
GET /transactions/account/1
```

Response:

```json
[
  {
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "amount": 100.00,
    "createdAt": "2026-06-07T15:00:00"
  },
  {
    "id": 2,
    "sourceAccountId": 3,
    "destinationAccountId": 1,
    "amount": 50.00,
    "createdAt": "2026-06-07T15:30:00"
  }
]
```

## Tratamento de Erros

### Conta não encontrada

```http
404 Not Found
```

```json
{
  "code": "ACCOUNT_NOT_FOUND",
  "message": "Account not found: 999",
  "timestamp": "2026-06-05T17:25:01"
}
```

### Saldo insuficiente

```http
400 Bad Request
```

```json
{
  "code": "INSUFFICIENT_BALANCE",
  "message": "Insufficient balance for account: 1",
  "timestamp": "2026-06-05T17:25:01"
}
```

## Decisões de Design

* Utilização de Spring Boot para simplificar a construção da API.
* Banco H2 em memória para eliminar dependências externas e facilitar a execução do projeto.
* Persistência através de Spring Data JPA.
* Lombok para redução de código boilerplate.
* Uso de `@Transactional` para garantir consistência durante transferências.
* Controle de concorrência através de Optimistic Locking (`@Version`) na entidade `Account`.
* Separação de responsabilidades entre controllers, services e repositories.
* Exceções customizadas para tratamento de erros de negócio.
* Registro de todas as transferências realizadas para fins de auditoria e consulta de movimentações.
* Notificações implementadas através de um serviço dedicado (`NotificationService`). Como o desafio não especifica um canal de comunicação (e-mail, SMS, push notification etc.), a notificação foi simulada através de logs da aplicação.

## Testes Implementados

* Criação de conta com sucesso
* Consulta de contas
* Transferência realizada com sucesso
* Saldo insuficiente
* Conta inexistente
* Registro de movimentação após transferência
* Consulta de movimentações por conta
* Consulta de todas as movimentações
* Controle de concorrência via Optimistic Locking
