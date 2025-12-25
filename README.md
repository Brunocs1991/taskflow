# TaskFlow 🗂️

TaskFlow é um projeto de portfólio desenvolvido com foco em **boas práticas de engenharia**, **arquitetura limpa** e **ambiente reproduzível via Docker**, demonstrando um backend moderno em Java com Micronaut integrado a MongoDB e um frontend simples em React.

O objetivo do projeto é mostrar **capacidade técnica real**, não apenas código que “funciona”.

---

## 🎯 Objetivo do Projeto

- Demonstrar domínio de **Java backend moderno**
- Aplicar **arquitetura em camadas**
- Integrar com banco NoSQL (MongoDB)
- Criar um frontend funcional e claro
- Subir tudo com **1 comando via Docker Compose**
- Servir como **case técnico para LinkedIn e GitHub**

---

## 🧱 Arquitetura Geral

## Estrutura do Projeto

```
taskflow/
├── backend/    # Micronaut + Java + MongoDB
├── frontend/   # React + Vite (build estático)
├── infra/      # Docker Compose
└── README.md   # Documentação do projeto
```

## 🛠️ Stack Utilizada

### Backend
- Java 21
- Micronaut 4
- Micronaut Data MongoDB
- Bean Validation
- OpenAPI / Swagger
- Maven

### Frontend
- React
- Vite
- JavaScript
- Build estático servido via Nginx

### Infraestrutura
- Docker
- Docker Compose
- MongoDB
- Mongo Express (ambiente local)

---

## 📦 Funcionalidades

### Tasks
- Criar task
- Listar tasks com paginação
- Buscar por texto
- Filtrar por status (`TODO`, `DOING`, `DONE`)
- Atualizar task
- Remover task

### Comentários
- Adicionar comentários por task
- Listar comentários de uma task
- Validação de task existente

---

## 📘 Documentação da API

A API é documentada automaticamente via **OpenAPI (Swagger)**.

Após subir o backend:

- Swagger UI:  
  👉 http://localhost:8080/swagger-ui/index.html

---

## 🚀 Como rodar o projeto (1 comando)

### Pré-requisitos
- Docker
- Docker Compose

### Subir a stack completa

```bash
cd infra
docker compose -f docker-compose.app.yml up -d --build
