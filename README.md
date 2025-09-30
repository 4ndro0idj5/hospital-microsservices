# 🏥 Agendamento Service

Microserviço responsável pelo **agendamento de consultas médicas**, desenvolvido em **Java 21 + Spring Boot 3**, utilizando **GraphQL**, **Spring Security**, **RabbitMQ** para mensageria e **PostgreSQL** como banco de dados.

---

## 📌 Funcionalidades

- 📅 Criar e atualizar consultas.  
- 🔎 Consultar consultas futuras de um paciente.  
- 🧑‍⚕️ Médicos e enfermeiros podem visualizar consultas de pacientes.  
- 👤 Pacientes podem consultar apenas suas próprias consultas futuras.  
- 📢 Integração com **RabbitMQ** para envio de notificações ao microserviço de notificações.  
- 🔐 Segurança via **Spring Security** com autenticação **Basic Auth** (usuários em memória).  

---

## 🏗 Arquitetura

### 🔹 Componentes

- **Spring Boot 3 + Java 21** – Core do microserviço  
- **Spring Data JPA** – Persistência com PostgreSQL  
- **Spring Security** – Autenticação e autorização (Basic Auth)  
- **Spring GraphQL** – API para consultas e mutations  
- **RabbitMQ** – Mensageria para notificações assíncronas  
- **Docker & Docker Compose** – Provisionamento de infraestrutura  

### 🔹 Diagrama Simplificado

```
        ┌─────────────┐         ┌─────────────┐
        │   Paciente  │         │   Médico    │
        └──────┬──────┘         └──────┬──────┘
               │                       │
               ▼                       ▼
         ┌───────────────────────────────────────┐
         │         Agendamento Service            │
         │   - GraphQL API                        │
         │   - Security (Basic Auth)              │
         │   - JPA (PostgreSQL)                   │
         │   - Publica eventos no RabbitMQ        │
         └───────────────────┬───────────────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │ Notificacao Service │
                  │   - Consome fila    │
                  │   - Envia alertas   │
                  └─────────────────────┘
```

---

## ⚙️ Setup do Ambiente

### 1️⃣ Dependências

- **Java 21**  
- **Maven 3.9+**  
- **Docker + Docker Compose**  

### 2️⃣ Subir infraestrutura

Com o arquivo `docker-compose.yml` já existente:

```bash
docker-compose up -d
```

Isso provisiona:

- **Postgres** em `localhost:5432`  
  - Banco: `hospital`  
  - Usuário: `postgres`  
  - Senha: `postgres`  

- **RabbitMQ** em `localhost:5672`  
  - Painel de administração: [http://localhost:15672](http://localhost:15672)  
  - Usuário: `guest`  
  - Senha: `guest`  

### 3️⃣ Executar aplicação localmente

```bash
mvn spring-boot:run
```

---

## 🔑 Segurança

O sistema utiliza **Spring Security** com usuários em memória:

| Usuário   | Senha      | Papel           |
|-----------|-----------|-----------------|
| medico    | medico123 | ROLE_MEDICO     |
| enf       | enf123    | ROLE_ENFERMEIRO |
| paciente  | pac123    | ROLE_PACIENTE   |

---

## 📡 API GraphQL

### URL base
```
POST http://localhost:8080/graphql
```

### Headers obrigatórios
```
Content-Type: application/json
Authorization: Basic <credenciais>
```

---

### 🔹 Queries disponíveis

#### Buscar consulta por ID
```json
{
  "query": "query { consultaById(id: 1) { id pacienteId motivo dataHora status } }"
}
```

#### Consultar consultas de um paciente (médico/enfermeiro)
```json
{
  "query": "query { consultasByPaciente(pacienteId: 100) { id motivo dataHora status } }"
}
```

#### Consultar consultas futuras (paciente)
```json
{
  "query": "query { consultasFuturas { id motivo dataHora status } }"
}
```

---

### 🔹 Mutations disponíveis

#### Criar consulta
```json
{
  "query": "mutation { criarConsulta(input: { pacienteId: 100, medicoId: 10, motivo: \"Dor de cabeça\", dataHora: \"2025-09-15T09:30:00Z\" }) { id pacienteId dataHora status } }"
}
```

#### Atualizar consulta
```json
{
  "query": "mutation { atualizarConsulta(id: 1, input: { pacienteId: 100, motivo: \"Consulta de retorno\", dataHora: \"2025-09-20T10:00:00Z\" }) { id motivo dataHora status } }"
}
```

---

## 📢 Mensageria

Sempre que uma consulta é **criada** ou **atualizada**, é publicada uma mensagem na fila `notificacao-consulta`.

### Exemplo de mensagem no RabbitMQ

```json
{
  "pacienteId": 100,
  "medicoId": 10,
  "mensagem": "Sua consulta foi agendada.",
  "dataHora": "2025-09-15T09:30:00Z"
}
```

---

## 🐳 Executando com Docker

### Gerar a imagem
```bash
mvn clean package -DskipTests
docker build -t agendamento-service .
```

### Rodar container
```bash
docker run -p 8080:8080 --name agendamento-service agendamento-service
```

---

## 🧪 Testes no Postman

Para facilitar os testes, você pode importar a Collection pronta do Postman:

📥 [Baixar Collection Postman](./postman/agendamento_service_graphql_collection.json)

✍️ Autor: **Anderson Argollo**  
📌 Pós-Tech FIAP – Arquitetura e Desenvolvimento Java  
