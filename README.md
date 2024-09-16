---

# 📧 Projeto de Administração de E-mail

Este projeto é uma API para gestão de caixas de e-mail, pastas e mensagens. Foi desenvolvido usando as seguintes tecnologias:

- **Java 17**
- **Spring**
- **PostgreSQL**
- **Spring Validation**
- **JPA**
- **Junit**
- **Docker**
- **Postman**

---

## 🚀 Como Rodar o Projeto

Para rodar o projeto, siga estes passos:

1. **Instale o Docker e o Docker Compose**:
   - Se ainda não tem, baixe e instale a partir do [site do Docker](https://www.docker.com/products/docker-desktop).

2. **Prepare o Ambiente**:
   - Navegue até a pasta principal do projeto onde está o arquivo `docker-compose.yml`.

3. **Inicie o Projeto**:
   - Abra o terminal e execute:
     ```bash
     docker-compose up --build -d
     ```
   - Isso vai criar e iniciar os containers necessários.

4. **Verifique o Status**:
   - Para confirmar que tudo está funcionando, use:
     ```bash
     docker-compose ps
     ```

5. **Teste a API**:
   - Use o arquivo `email.postman_collection.json` para testar os endpoints.
   - No Postman:
     - Abra o aplicativo.
     - Clique em "Importar" no canto superior esquerdo.
     - Selecione o arquivo e clique em "Importar".

---

## 🗃️ Script de Banco de Dados

```sql
CREATE TABLE IF NOT EXISTS MAILBOX (
    idt SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS FOLDER (
    idt SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    mailbox_id INT NOT NULL,
    FOREIGN KEY (mailbox_id) REFERENCES MAILBOX(idt) ON DELETE CASCADE,
    UNIQUE (name, mailbox_id)
);

CREATE TABLE IF NOT EXISTS MESSAGE (
    idt SERIAL PRIMARY KEY,
    sender TEXT NOT NULL,
    recipient TEXT NOT NULL,
    subject TEXT,
    body TEXT,
    read BOOLEAN NOT NULL,
    send_at TIMESTAMP NOT NULL,
    folder_id INT NOT NULL,
    FOREIGN KEY (folder_id) REFERENCES FOLDER(idt) ON DELETE CASCADE
);
```

---

## 📝 Avaliação Desenvolvedor Junior Backend

### 🛠️ Tarefas

1. Desenvolver uma API HTTP Restful em Kotlin/Java para a gestão de caixas de e-mail, pastas e mensagens armazenadas.
2. Criar um arquivo `README.md` no formato Markdown com instruções sobre como executar o projeto e outras informações necessárias.

![image](https://github.com/user-attachments/assets/d4288bfa-8d67-4f58-8adc-ee365a892d6a)

### 📡 API

Todas as chamadas que têm corpo na requisição e resposta deverão estar no formato JSON. A seguir, os recursos a serem desenvolvidos. Implemente as validações citadas, mas sinta-se livre para adicionar outras que julgar necessário:

#### 1. Criação de Caixa (Mailbox)

Desenvolva um recurso para criação de uma caixa (mailbox). Ao criar uma caixa, as pastas (folder) INBOX, JUNK e SENT deverão ser criadas. Não poderão existir caixas duplicadas. O nome da caixa deve ter o formato de e-mail (exemplo: `localpart@domain`).

**URL:** `/api/v1/mailboxes`  
**Método:** POST

**Exemplo de corpo da requisição:**

```json
{
  "name": "teste@dominio.com"
}
```

**Retornos da API:**

- **201** - Caixa Criada 🎉
- **400** - Nome da caixa é inválido 🚫
- **409** - Caixa já existe ⚠️

#### 2. Criação de Pasta (Folder)

Desenvolva um recurso para criação de pasta (folder) associada a uma caixa. Não poderão existir pastas duplicadas para uma mesma caixa. Os caracteres permitidos para uma pasta são letras a-z, A-Z, hífen (-), underline (_), limitado a até 100 caracteres.

**URL:** `/api/v1/mailboxes/{mailbox}/folders`  
**Método:** POST

**Exemplo de corpo da requisição:**

```json
{
  "name": "arquivos-de-testes"
}
```

**Retornos da API:**

- **201** - Pasta criada 🎉
- **404** - Mailbox não existe 🚫
- **409** - Pasta já existe ⚠️
- **400** - Pasta inválida 🚫

#### 3. Envio de Mensagem

Desenvolva um recurso para salvar o envio de uma mensagem de uma caixa. Deverá obrigatoriamente receber o nome da caixa que executou o envio e o nome do e-mail destinatário, ambos devem ter o formato de e-mail (exemplo: `localpart@domain`). Opcionalmente, o assunto (se informado, limitado a 50 caracteres) e o corpo do e-mail. Ao salvar um envio, deverá ser armazenado na tabela `message` associado à pasta SENT e marcada como lida.

**URL:** `/api/v1/mailboxes/{mailbox}/send-message`  
**Método:** POST

**Exemplo de corpo da requisição:**

```json
{
  "recipient": "teste@dominio.com",
  "subject": "Assunto de teste",
  "body": "Corpo do e-mail de teste"
}
```

**Retornos da API:**

- **201** - Mensagem armazenada 🎉
- **404** - Mailbox não existe 🚫
- **400** - Destinatário inválido 🚫
- **400** - Assunto inválido 🚫

#### 4. Recebimento de Mensagem

Desenvolva um recurso para salvar o recebimento de uma mensagem em uma caixa. Deverá obrigatoriamente receber o nome da caixa que recebeu a mensagem e o nome do e-mail remetente, ambos devem ter o formato de e-mail (exemplo: `localpart@domain`). Opcionalmente, o assunto (se informado, limitado a 50 caracteres), corpo do e-mail e o nome da pasta onde a mensagem deve ser armazenada (caso não informado, deverá ser INBOX; caso não exista, retorna falha). Ao salvar um recebimento, deverá ser armazenado na tabela `message` associado à pasta INBOX ou a informada no corpo da requisição.

**URL:** `/api/v1/mailboxes/{mailbox}/receive-message`  
**Método:** POST

**Exemplo de corpo da requisição:**

```json
{
  "sender": "teste@dominio.com",
  "subject": "Assunto de teste",
  "body": "Corpo do e-mail de teste",
  "folder": "JUNK"
}
```

**Retornos da API:**

- **201** - Mensagem armazenada 🎉
- **404** - Mailbox não existe 🚫
- **400** - Destinatário inválido 🚫
- **400** - Assunto inválido 🚫
- **400** - Pasta já existe 🚫

#### 5. Marcar Mensagem como Lida ou Não Lida

Desenvolva um recurso para marcar uma mensagem como lida ou não lida. Deverá obrigatoriamente receber o nome da caixa, identificador (idt) da pasta e identificador (idt) da mensagem.

**URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read`  
**Método:** PUT

**Exemplo de corpo da requisição:**

```json
{
  "read": true
}
```

**Retornos da API:**

- **204** - Mensagem atualizada 🎉
- **404** - Mailbox não existe 🚫
- **400** - Pasta não existe 🚫
- **400** - Mensagem não existe 🚫

#### 6. Listar Caixas Armazenadas

Desenvolva um recurso para listar as caixas armazenadas.

**URL:** `/api/v1/mailboxes`  
**Método:** GET

**Exemplo de corpo da resposta:**

```json
[
  {
    "name": "teste@uol.com.br"
  },
  {
    "name": "avaliacao@dominio.com"
  }
]
```

**Retornos da API:**

- **200** - Devolve a lista de Mailboxes 📋

#### 7. Listar Pastas de uma Caixa

Desenvolva um recurso para listar as pastas de uma caixa. Deverá obrigatoriamente receber o nome da caixa.

**URL:** `/api/v1/mailboxes/{mailbox}/folders`  
**Método:** GET

**Exemplo de corpo da resposta:**

```json
[
  {
    "idt": 1,
    "name": "INBOX"
  },
  {
    "idt": 2,
    "name": "JUNK"
  }
]
```

**Retornos da API:**

- **200** - Devolve a lista de Pastas 📋
- **404** - Mailbox não existe 🚫

#### 8. Listar Mensagens de uma Pasta

Desenvolva um recurso para listar as mensagens de uma pasta de uma caixa. Deverá obrigatoriamente receber o nome da caixa e identificador (idt) da pasta.

**URL:** `/api/v1/mailboxes

/{mailbox}/folders/{folderIdt}/messages`  
**Método:** GET

**Exemplo de corpo da resposta:**

```json
[
  {
    "idt": 1,
    "sender": "teste@dominio.com",
    "recipient": "exemplo@dominio.com",
    "subject": "Teste",
    "send_at": "2024-09-01T12:34:56Z",
    "read": true
  }
]
```

**Retornos da API:**

- **200** - Devolve a lista de Mensagens 📋
- **404** - Mailbox ou Pasta não existe 🚫

#### 9. Detalhamento de Mensagem

Desenvolva um recurso para detalhar uma mensagem de uma pasta de uma caixa. Deverá obrigatoriamente receber o nome da caixa, identificador (idt) da pasta e identificador (idt) da mensagem.

**URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}`  
**Método:** GET

**Exemplo de corpo da resposta:**

```json
{
  "idt": 1,
  "sender": "teste@dominio.com",
  "recipient": "exemplo@dominio.com",
  "subject": "Teste",
  "body": "Corpo do e-mail",
  "send_at": "2024-09-01T12:34:56Z",
  "read": true
}
```

**Retornos da API:**

- **200** - Detalhamento da Mensagem 📋
- **404** - Mailbox, Pasta ou Mensagem não existe 🚫
