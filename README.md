Claro! Aqui está o texto formatado em Markdown para o seu README do GitHub:

```markdown
# Passos Necessários para Rodar o Projeto

Na pasta raiz da aplicação, execute o comando no terminal para criar a imagem do Docker:

```bash
docker-compose up --build -d
```

## Avaliação Desenvolvedor Junior Backend

### Tarefas

1. Desenvolver uma API HTTP Restful em Kotlin/Java para gestão de caixas de e-mail, pastas e mensagens armazenadas.
2. Criar um arquivo `README.md` no formato Markdown com instruções de como executar o projeto, dentre outras que julgar necessário.

![image](https://github.com/user-attachments/assets/d4288bfa-8d67-4f58-8adc-ee365a892d6a)

### API

Todas as chamadas que têm corpo na requisição e resposta deverão estar no formato JSON. A seguir, os recursos a serem desenvolvidos:

#### 1. Criação de Caixa (Mailbox)

Desenvolver um recurso para criação de caixa (mailbox). Ao criar uma caixa, as pastas (folder) INBOX, JUNK e SENT deverão ser criadas. Não poderá ter caixas duplicadas. O nome da caixa deve ter o formato de e-mail (exemplo “localpart@domain”).

- **URL**: `/api/v1/mailboxes` – **Método**: POST
- **Exemplo de Corpo da Requisição**:

```json
{
  "name": "teste@dominio.com"
}
```

![image](https://github.com/user-attachments/assets/1b91bfdf-583f-4c47-860e-38bb9d4185e4)

- **Retornos da API**:
  - `201` - Caixa Criada
  - `400` - Nome da caixa é inválido
  - `409` - Caixa já existe

#### 2. Criação de Pasta (Folder)

Desenvolver um recurso para criação de pasta (folder) associada a uma caixa. Não poderá ter pastas duplicadas para uma mesma caixa. Os caracteres permitidos são letras a-z, A-Z, hífen (-), underline (_), limitado a até 100 caracteres.

- **URL**: `/api/v1/mailboxes/{mailbox}/folders` – **Método**: POST
- **Exemplo de Corpo da Requisição**:

```json
{
  "name": "arquivos-de-testes"
}
```

![image](https://github.com/user-attachments/assets/c3491591-5b27-41f6-b11e-092c84645ac2)

- **Retornos da API**:
  - `201` - Pasta criada
  - `404` - Mailbox não existe
  - `409` - Pasta já existe
  - `400` - Pasta inválida

#### 3. Envio de Mensagem

Desenvolver um recurso para salvar o envio de uma mensagem de uma caixa. Receberá obrigatoriamente o nome da caixa que executou o envio e o nome do e-mail destinatário, ambos devem ter o formato de e-mail. Opcionalmente, o assunto (se informado, limitado a 50 caracteres), corpo do e-mail.

- **URL**: `/api/v1/mailboxes/{mailbox}/send-message` – **Método**: POST
- **Exemplo de Corpo da Requisição**:

```json
{
  "recipient": "teste@dominio.com",
  "subject": "Assunto de teste",
  "body": "Corpo do e-mail de teste"
}
```

![image](https://github.com/user-attachments/assets/a58052d0-4ce2-405e-8eb2-c76ee4c8114f)

- **Retornos da API**:
  - `201` - Mensagem armazenada
  - `404` - Mailbox não existe
  - `400` - Destinatário inválido
  - `400` - Assunto inválido

#### 4. Recebimento de Mensagem

Desenvolver um recurso para salvar o recebimento de uma mensagem em uma caixa. Receberá obrigatoriamente o nome da caixa que recebeu a mensagem e o nome do e-mail remetente, ambos devem ter o formato de e-mail. Opcionalmente, o assunto (se informado, limitado a 50 caracteres), corpo do e-mail e o nome da pasta que deve armazenar a mensagem.

- **URL**: `/api/v1/mailboxes/{mailbox}/receive-message` – **Método**: POST
- **Exemplo de Corpo da Requisição**:

```json
{
  "sender": "teste@dominio.com",
  "subject": "Assunto de teste",
  "body": "Corpo do e-mail de teste",
  "folder": "JUNK"
}
```

![image](https://github.com/user-attachments/assets/7b97bdbc-a34b-4da4-a061-901f5ee73c5d)

- **Retornos da API**:
  - `201` - Mensagem armazenada
  - `404` - Mailbox não existe
  - `400` - Remetente inválido
  - `400` - Assunto inválido
  - `400` - Pasta já existe

#### 5. Marcar Mensagem como Lida/Não Lida

Desenvolver um recurso para marcar uma mensagem como lida ou não lida. Receberá obrigatoriamente o nome da caixa, identificador (ID) da pasta e identificador (ID) da mensagem.

- **URL**: `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read` – **Método**: PUT
- **Exemplo de Corpo da Requisição**:

```json
{
  "read": true
}
```

![image](https://github.com/user-attachments/assets/8f7bc908-d489-42bd-8a44-1273cdaf93e1)

- **Retornos da API**:
  - `204` - Mensagem atualizada
  - `404` - Mailbox não existe
  - `400` - Pasta não existe
  - `400` - Mensagem não existe

#### 6. Listar Caixas

Desenvolver um recurso para listar as caixas armazenadas.

- **URL**: `/api/v1/mailboxes` – **Método**: GET
- **Exemplo de Corpo da Resposta**:

```json
[
  {
    "name": "teste@uol.com.br"
  },
  {
    "name": "avaliacao@bol.com.br"
  }
]
```

- **Retorno da API**:
  - `200` - Devolve a lista de caixas

#### 7. Listar Pastas de uma Caixa

Desenvolver um recurso para listar as pastas de uma caixa. Receberá obrigatoriamente o nome da caixa.

- **URL**: `/api/v1/mailboxes/{mailbox}/folders` – **Método**: GET
- **Exemplo de Corpo da Resposta**:

```json
[
  {
    "idt": 1,
    "name": "INBOX"
  },
  {
    "idt": 2,
    "name": "JUNK"
  },
  {
    "idt": 3,
    "name": "SENT"
  }
]
```

![image](https://github.com/user-attachments/assets/51cdfdb9-eb5a-426d-8889-8613fce8f675)

- **Retornos da API**:
  - `200` - Devolve a lista de pastas
  - `404` - Mailbox não existe

#### 8. Listar Mensagens de uma Caixa e Pasta

Desenvolver um recurso para listar as mensagens de uma caixa e pasta. Receberá obrigatoriamente o nome da caixa e da pasta.

- **URL**: `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages` – **Método**: GET
- **Exemplo de Corpo da Resposta**:

```json
[
  {
    "idt": 1,
    "sender": "teste@uol.com.br",
    "subject": "Assunto de teste",
    "sent_at": "2006-01-02 15:04:05",
    "read": true
  },
  {
    "idt": 2,
    "sender": "test@uh.com.br",
    "subject": "Assunto de testado",
    "sent_at": "2023-01-02 15:04:05",
    "read": false
  }
]
```

![image](https://github.com/user-attachments/assets/83aca641-66dc-4054-94ca-aa4f8adecb90)

- **Retornos da API**:
  - `200` - Devolve a lista de mensagens
  - `404` - Mailbox não existe
  - `404` - Folder não existe

#### 9. Visualizar Detalhes da Mensagem

Desenvolver um recurso para visualizar os detalhes da mensagem. Receberá obrigatoriamente o nome da caixa, nome da pasta e identificador da mensagem.

- **URL**: `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}` – **Método**: GET
- **Exemplo de Corpo da Resposta**:

```json
{
  "idt": 2,
  "sender": "test@uh.com

.br",
  "subject": "Assunto de testado",
  "body": "Corpo do e-mail testado",
  "sent_at": "2023-01-02 15:04:05",
  "read": false
}
```

![image](https://github.com/user-attachments/assets/abe18f94-1c1d-4673-b2bc-e77c1d92f0b0)

- **Retornos da API**:
  - `200` - Devolve os detalhes da mensagem
  - `404` - Mailbox não existe
  - `404` - Folder não existe
  - `404` - Mensagem não existe

### Instruções de Execução

1. **Certifique-se de que o Docker e o Docker Compose estão instalados.**
2. **Na raiz do projeto, execute:**

```bash
docker-compose up --build -d
```

3. **Acesse a API através dos endpoints especificados.**

### Configurações Adicionais

- **Banco de Dados**: Verifique as configurações do banco de dados no arquivo `docker-compose.yml` e ajuste conforme necessário.

```

Se precisar de mais alguma alteração ou ajuste, é só avisar!
