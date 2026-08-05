# Plataforma de Ideias e Inovação

## :octocat: Integrantes

[Laissa Gama](https://github.com/laissagamma) | [Mário Ramon](https://github.com/joyeuxpierrot) | [Pedro Duarte](https://github.com/PedrokaIsACoder) | [José Jonathan](https://github.com/jonathanbraga47) | [Antonio Victor](https://github.com/vieiraAnttonio)

## 📃 Sobre o Projeto

Projeto desenvolvido para a disciplina de **Engenharia de Software**, ministrada pela Professora [Thaís Alves Burity Rocha](https://github.com/taburity), da UFAPE (campus Garanhuns), referente à 2ª Verificação de Aprendizagem.

O sistema se propõe a catalogar as ideias da comunidade para melhorar a UFAPE, oferecendo um espaço centralizado onde estudantes, docentes e servidores podem propor melhorias, discuti-las coletivamente e acompanhar seu andamento até a implantação.

## 📍 Objetivos

O usuário deve ser capaz de cadastrar propostas de melhoria para a universidade, comentar e discutir as propostas de outros usuários, e votar nas ideias que considera mais relevantes. Cada proposta possui um status que evolui ao longo do tempo (submetida, em análise, aprovada, implantada ou rejeitada), permitindo o acompanhamento do seu ciclo de vida. O sistema também conta com um dashboard de indicadores, exibindo métricas sobre as propostas ao longo do tempo, como volume de submissões, taxa de aprovação e engajamento da comunidade.

## 🛠️ Tecnologias Usadas

### Frontend
- [Angular](https://angular.dev/)

### Backend
- [Java](https://www.java.com/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA]
- [Spring Security]
- [Maven]

### Banco de Dados

- MySQL 8.4
- Docker (para execução do banco)

## Como executar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/Grupo-4-ES-2026-1/Plataforma-de-Ideias-e-Inovacao.git
```

Entre na pasta do projeto:

```bash
cd Plataforma-de-Ideias-e-Inovacao
```

---

### 2. Subir o banco de dados

Na raiz do projeto execute:

```bash
docker-compose up -d
```

Isso iniciará um container MySQL com:

- Banco: `plataforma_ideias`
- Usuário: `grupo4`
- Senha: `123456`

### 3. Executar o backend

Entre na pasta do backend:

```bash
cd backend
```

Execute:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

## Configuração do banco

O backend utiliza as seguintes configurações:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plataforma_ideias
spring.datasource.username=grupo4
spring.datasource.password=123456
```

### Gerenciamento
- GitHub Projects (Quadro Scrum)

## 🚧 Status do Projeto

Em desenvolvimento — Primeira Iteração
