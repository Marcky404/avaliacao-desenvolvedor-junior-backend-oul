
```markdown
# Passos Necessários para Rodar o Projeto

1. **Crie a imagem do Docker:**

   Na pasta raiz da aplicação, abra o terminal e rode o seguinte comando para criar a imagem do Docker:

   ```bash
   docker-compose up --build -d
   ```

   Esse comando vai compilar o projeto e subir os contêineres em segundo plano.

2. **Verifique se o contêiner está rodando:**

   Após rodar o comando acima, digite o seguinte para confirmar se está tudo certo:

   ```bash
   docker ps
   ```

```

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

## Avaliação Desenvolvedor Junior Backend

### Tarefas

1. Desenvolver uma API HTTP Restful em Kotlin/Java para a gestão de caixas de e-mail, pastas e mensagens armazenadas.
2. Criar um arquivo `README.md` em formato Markdown com instruções sobre como executar o projeto e outras informações necessárias.

![image](https://github.com/user-attachments/assets/d4288bfa-8d67-4f58-8adc-ee365a892d6a)

### API

Todas as chamadas que têm corpo na requisição e resposta devem estar no formato JSON. Abaixo estão os recursos a serem desenvolvidos:

1. **Criação de Caixa (Mailbox)**
   - **URL:** `/api/v1/mailboxes` 
   - **Método:** POST
   - **Exemplo de Corpo de Requisição:**
     ```json
     {
       "name": "teste@dominio.com"
     }
     ```
   - **Retornos da API:**
     - `201` - Caixa Criada
     - `400` - Nome da caixa é inválido
     - `409` - Caixa já existe

   ![image](https://github.com/user-attachments/assets/1b91bfdf-583f-4c47-860e-38bb9d4185e4)

2. **Criação de Pasta (Folder)**
   - **URL:** `/api/v1/mailboxes/{mailbox}/folders`
   - **Método:** POST
   - **Exemplo de Corpo de Requisição:**
     ```json
     {
       "name": "arquivos-de-testes"
     }
     ```
   - **Retornos da API:**
     - `201` - Pasta criada
     - `404` - Mailbox não existe
     - `409` - Pasta já existe
     - `400` - Pasta inválida

   ![image](https://github.com/user-attachments/assets/c3491591-5b27-41f6-b11e-092c84645ac2)

3. **Envio de Mensagem**
   - **URL:** `/api/v1/mailboxes/{mailbox}/send-message`
   - **Método:** POST
   - **Exemplo de Corpo de Requisição:**
     ```json
     {
       "recipient": "teste@dominio.com",
       "subject": "Assunto de teste",
       "body": "Corpo do e-mail de teste"
     }
     ```
   - **Retornos da API:**
     - `201` - Mensagem armazenada
     - `404` - Mailbox não existe
     - `400` - Destinatário inválido
     - `400` - Assunto inválido

   ![image](https://github.com/user-attachments/assets/a58052d0-4ce2-405e-8eb2-c76ee4c8114f)

4. **Recebimento de Mensagem**
   - **URL:** `/api/v1/mailboxes/{mailbox}/receive-message`
   - **Método:** POST
   - **Exemplo de Corpo de Requisição:**
     ```json
     {
       "sender": "teste@dominio.com",
       "subject": "Assunto de teste",
       "body": "Corpo do e-mail de teste",
       "folder": "JUNK"
     }
     ```
   - **Retornos da API:**
     - `201` - Mensagem armazenada
     - `404` - Mailbox não existe
     - `400` - Remetente inválido
     - `400` - Assunto inválido
     - `400` - Pasta não existe

   ![image](https://github.com/user-attachments/assets/7b97bdbc-a34b-4da4-a061-901f5ee73c5d)

5. **Marcar Mensagem como Lida ou Não Lida**
   - **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read`
   - **Método:** PUT
   - **Exemplo de Corpo de Requisição:**
     ```json
     {
       "read": true
     }
     ```
   - **Retornos da API:**
     - `204` - Mensagem atualizada
     - `404` - Mailbox não existe
     - `400` - Pasta não existe
     - `400` - Mensagem não existe

   ![image](https://github.com/user-attachments/assets/8f7bc908-d489-42bd-8a44-1273cdaf93e1)

6. **Listar Caixas Armazenadas**
   - **URL:** `/api/v1/mailboxes`
   - **Método:** GET
   - **Exemplo de Corpo de Resposta:**
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
   - **Retorno da API:**
     - `200` - Devolve a lista de caixas

7. **Listar Pastas de uma Caixa**
   - **URL:** `/api/v1/mailboxes/{mailbox}/folders`
   - **Método:** GET
   - **Exemplo de Corpo de Resposta:**
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
   - **Retornos da API:**
     - `200` - Devolve a lista de pastas
     - `404` - Mailbox não existe

   ![image](https://github.com/user-attachments/assets/51cdfdb9-eb5a-426d-8889-8613fce8f675)

8. **Listar Mensagens de uma Caixa e Pasta**
   - **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages`
   - **Método:** GET
   - **Exemplo de Corpo de Resposta:**
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
   - **Retornos da API:**
     - `200` - Devolve a lista de mensagens
     - `404` - Mailbox não existe
     - `404` - Pasta não existe

   ![image](https://github.com/user-attachments/assets/83aca641-66dc-4054-94ca-aa4f8adecb90)

9. **Visualizar Detalhes da Mensagem**
   - **URL:** `/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}`
   - **Método:** GET
   - **Exemplo de Corpo de Resposta:**
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
   - **Retornos da API:**
     - `200` - Devolve os dados da mensagem
     - `404` - Mailbox não existe
     - `404` - Pasta não existe
     - `404` - Mensagem não existe

   ![image](https://github.com/user-attachments/assets/029d56e7-96d3-4a4e-a95d-052481037286)

### Bônus

1. Criar testes unitários e/ou integração.
2. Implementar a execução do projeto utilizando Docker e/ou Docker Compose.
3. Criar um recurso /v2 para os recursos 6, 7 e 8 que implementem algum
meio de paginação.


