# Upload de Dados no MongoDB Local — TaskFlow

Este documento descreve como inserir, importar e validar dados em um MongoDB local utilizado pelo projeto TaskFlow, executado via Docker Compose.

---

## Pré-requisitos

- Docker e Docker Compose instalados
- Stack de infraestrutura do projeto em execução

Para subir o MongoDB e o Mongo Express:

```bash
cd infra
docker compose up -d
```

---

## Acesso ao MongoDB

### Via Mongo Express (Interface Web)

O Mongo Express permite visualizar e manipular dados diretamente pelo navegador.

- URL: http://localhost:8081
- Autenticação: desabilitada (apenas para ambiente local)

**Passos:**
1. Acesse o link acima
2. Clique em **Create Database**
3. Nome do banco: `taskflow`
4. Nome da coleção: `tasks`

---

## Inserção de Dados via mongosh (Recomendado)

### Acessar o container do MongoDB

```bash
docker exec -it taskflow-mongo mongosh -u root -p root --authenticationDatabase admin
```

### Criar ou selecionar o database

```js
use taskflow
```

### Inserir um documento de exemplo

```js
db.tasks.insertOne({
  title: "Primeira Task",
  description: "Task criada manualmente para testes locais",
  status: "TODO",
  createdAt: new Date(),
  updatedAt: new Date()
})
```

### Listar dados inseridos

```js
db.tasks.find().pretty()
```

---

## Importação de Dados via JSON

Útil para carga inicial ou simulação de massa de dados.

### Exemplo de arquivo JSON (`tasks.json`)

```json
[
  {
    "title": "Estudar Micronaut",
    "description": "Criar base do backend com Java",
    "status": "TODO",
    "createdAt": "2025-01-01T10:00:00Z"
  },
  {
    "title": "Configurar Docker",
    "description": "Subir MongoDB via Docker Compose",
    "status": "DONE",
    "createdAt": "2025-01-02T14:30:00Z"
  }
]
```

### Copiar o arquivo para o container

```bash
docker cp tasks.json taskflow-mongo:/tmp/tasks.json
```

### Importar os dados

```bash
docker exec -it taskflow-mongo mongoimport -u root -p root --authenticationDatabase admin --db taskflow --collection tasks --file /tmp/tasks.json --jsonArray
```

### Validar importação

```js
db.tasks.find().pretty()
```

---

## Observações Importantes

- Este setup **não deve ser usado em produção**
- Credenciais estão expostas apenas para facilitar o ambiente local
- O volume Docker `mongo_data` garante persistência entre reinicializações

---

## Limpeza do Ambiente (Opcional)

Remover containers mantendo dados:

```bash
docker compose down
```

Remover containers **e dados**:

```bash
docker compose down -v
```

---

## Finalidade no Portfólio

Este documento demonstra:
- Conhecimento prático de MongoDB
- Uso de Docker para ambientes locais
- Capacidade de documentar processos técnicos de forma clara

Ele complementa o README principal e facilita a reprodução do projeto por terceiros.
