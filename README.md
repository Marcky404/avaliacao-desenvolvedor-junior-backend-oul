# API de Gestão de Caixas de E-mail

Este projeto é uma API HTTP RESTful desenvolvida em Kotlin/Java para a gestão de caixas de e-mail, pastas e mensagens armazenadas. A API permite criar caixas de e-mail, pastas, enviar e receber mensagens, e muito mais.

## Tecnologias

- Kotlin/Java
- Docker
- Docker Compose

## Requisitos

- Docker
- Docker Compose

## Passos Necessários para Executar o Projeto

1. **Clonar o Repositório**

    ```bash
    git clone https://github.com/your-username/your-repository.git
    cd your-repository
    ```

2. **Construir e Rodar o Projeto**

    Na pasta raiz da aplicação, execute o comando para criar a imagem do Docker e iniciar os containers:

    ```bash
    docker-compose up --build -d
    ```

3. **Acessar a API**

    A API estará disponível em `http://localhost:8080`.

## Documentação da API

### 1. Criar Caixa (Mailbox)

- **URL:** `/api/v1/mailboxes`
- **Método:** `POST`
- **Corpo da Requisição:**

    ```json
    {
      "name": "teste@dominio.com"
    }
    ```

- **Respostas:**
    - `201` - Caixa Criada
    - `400` - Nome da caixa é inválido
    - `409` - Caixa já existe

### 2. Criar Pasta (Folder)

- **URL:** `/api/v1/mailboxes/{mailbox}/folders`
- **Método:** `POST`
- **Corpo da Requisição:**

    ```json
    {
      "name": "arquivos-de-testes"
    }
    ```

- **Respostas:**
    - `201` - Pasta criada
    - `404` - Mailbox não existe
    - `409` - Pasta já existe
    - `400` - Pasta inválida

### 3. Enviar Mensagem

- **URL:** `/api/v1/mailboxes/{mailbox}/send-message`
- **Método:** `POST`
- **Corpo da Requisição:**

    ```json
    {
      "recipient": "teste@dominio.com",
      "subject": "Assunto de teste",
      "body": "Corpo do e-mail de teste"
    }
    ```

- **Respostas:**
    - `201` - Mensagem armazenada
    - `404` - Mailbox não existe
    - `400` - Destinatário inválido
    - `400` - Assunto inválido

### 4. Receber Mensagem

- **URL:** `/api/v1/mailboxes/{mailbox}/receive-message`
- **Método:** `POST`
- **Corpo da Requisição:**

    ```json
    {
      "sender": "teste@dominio.com",
      "subject": "Assunto de teste",
      "body": "Corpo do e-mail de teste",
      "folder": "JUNK"
    }
    ```

- **Respostas:**
    - `201` - Mensagem armazenada
    - `404` - Mailbox não existe
    - `400` - Remetente inválido
    - `400` - Assunto inválido
    - `400` - Pasta não existe

### 5. Marcar Mensagem como Lida ou Não Lida

- **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read`
- **Método:** `PUT`
- **Corpo da Requisição:**

    ```json
    {
      "read": true
    }
    ```

- **Respostas:**
    - `204` - Mensagem atualizada
    - `404` - Mailbox não existe
    - `400` - Pasta não existe
    - `400` - Mensagem não existe

### 6. Listar Caixas de E-mail

- **URL:** `/api/v1/mailboxes`
- **Método:** `GET`
- **Corpo da Resposta:**

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

- **Resposta:**
    - `200` - Devolve a lista de caixas

### 7. Listar Pastas de uma Caixa

- **URL:** `/api/v1/mailboxes/{mailbox}/folders`
- **Método:** `GET`
- **Corpo da Resposta:**

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

- **Respostas:**
    - `200` - Devolve a lista de pastas
    - `404` - Mailbox não existe

### 8. Listar Mensagens de uma Caixa e Pasta

- **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages`
- **Método:** `GET`
- **Corpo da Resposta:**

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

- **Respostas:**
    - `200` - Devolve a lista de mensagens
    - `404` - Mailbox não existe
    - `404` - Pasta não existe

### 9. Visualizar Detalhes da Mensagem

- **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}`
- **Método:** `GET`
- **Corpo da Resposta:**

    ```json
    {
      "idt": 2,
      "sender": "test@uh.com.br",
      "recipient": "destino@bol.com.br",
      "subject": "Assunto de testado",
      "body": "corpo da mensagem",
      "sent_at": "2023-01-02 15:04:05",
      "read": false
    }
    ```

- **Respostas:**
    - `200` - Devolve os dados da mensagem
    - `404` - Mailbox não existe
    - `404` - Pasta não existe
    - `404` - Mensagem não existe

## Recursos Bônus

1. **Criar Testes Unitários e/ou de Integração**
2. **Implementar a Execução do Projeto Utilizando Docker e/ou Docker Compose**
3. **Criar um Recurso `/v2` para os Recursos 6, 7 e 8 que Implementem Algum Meio de Paginação**

## Conclusão

Ao finalizar o projeto, o mesmo deve ser disponibilizado em algum repositório de código público (exemplo: GitHub, GitLab, Bitbucket, etc.) e o link compartilhado com a consultoria.

## Imagens

- ![Imagem 1](https://github.com/user-attachments/assets/d4288bfa-8d67-4f58-8adc-ee365a892d6a)
- ![Imagem 2](https://github.com/user-attachments/assets/1b91bfdf-583f-4c47-860e-38bb9d4185e4)
- ![Imagem 3](https://github.com/user-attachments/assets/c3491591-5b27-41f6-b11e-092c84645ac2)
- ![Imagem 4](https://github.com/user-attachments/assets/a58052d0-4ce2-405e-8eb2-c76ee4c8114f)
- ![Imagem 5](https://github.com/user-attachments/assets/7b97bdbc-a34b-4da4-a061-901f5ee73c5d)
- ![Imagem 6](https://github.com/user-attachments/assets/8f7bc908-d489-42bd-8a44-1273cdaf93e1)
- ![Imagem 7](https://github.com/user-attachments/assets/51cdfdb9-eb5a-426d-8889-8613fce8f675)
- ![Imagem 8](https://github.com/user-attachments/assets/83aca641-66dc-4054-94ca-aa4f8adecb90)
- ![Imagem 9](https://github.com/user-attachments/assets/029d56e7-96d3-4a4e-a95d-052481037286)
