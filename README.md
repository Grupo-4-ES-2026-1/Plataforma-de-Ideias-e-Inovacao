# 💡 Plataforma de Ideias e Inovação

Um espaço para a comunidade da UFAPE propor, discutir e acompanhar ideias de melhoria para a universidade.

## :octocat: Integrantes

| | | | | |
|:---:|:---:|:---:|:---:|:---:|
| [Laissa Gama](https://github.com/laissagamma) | [Mário Ramon](https://github.com/joyeuxpierrot) | [Pedro Duarte](https://github.com/PedrokaIsACoder) | [José Jonathan](https://github.com/jonathanbraga47) | [Antonio Victor](https://github.com/vieiraAnttonio) |

## 📃 Sobre o Projeto

Projeto desenvolvido para a disciplina de **Engenharia de Software**, ministrada pela Professora [Thaís Alves Burity Rocha](https://github.com/taburity), da UFAPE (campus Garanhuns), referente à 2ª Verificação de Aprendizagem.

O sistema se propõe a catalogar as ideias da comunidade para melhorar a UFAPE, oferecendo um espaço centralizado onde estudantes, docentes e servidores podem propor melhorias, discuti-las coletivamente e acompanhar seu andamento até a implantação.

## 📍 Objetivos

O usuário deve ser capaz de se cadastrar e autenticar no sistema, cadastrar propostas de melhoria para a universidade, e consultar as propostas já existentes. Cada proposta possui um status que evolui ao longo do tempo (submetida, em análise, aprovada, implantada ou rejeitada), permitindo o acompanhamento do seu ciclo de vida. Futuramente, o sistema também contará com comentários, votação e um dashboard de indicadores sobre as propostas ao longo do tempo.

## ✨ Funcionalidades já implementadas

- 🔐 **Autenticação e autorização** completas com JWT (cadastro, login e logout)
- 👤 Controle de acesso por perfil (**USER** / **ADMIN**)
- 📝 **Cadastro de propostas**, vinculado ao usuário autenticado
- 📋 **Listagem e detalhamento** de propostas
- 🛡️ Rotas protegidas no frontend (Guard) e no backend (Spring Security + Filter JWT)
- ✅ Testes automatizados (JUnit no backend, Vitest no frontend)

## 🛠️ Tecnologias Usadas

### Frontend
- [Angular](https://angular.dev/)
- JWT para autenticação, com interceptor HTTP e rotas protegidas por Guard

### Backend
- [Java](https://www.java.com/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security) + JWT
- [Maven](https://maven.apache.org/)

### Banco de Dados
- MySQL 8.4 (via Docker)

### Testes
- [JUnit 5](https://junit.org/junit5/) + Mockito (backend)
- [Vitest](https://vitest.dev/) (frontend)

### Gerenciamento
- GitHub Projects (Quadro Scrum)

---

## 🚀 Como executar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/Grupo-4-ES-2026-1/Plataforma-de-Ideias-e-Inovacao.git
cd Plataforma-de-Ideias-e-Inovacao
```

### 2. Subir o banco de dados

Na raiz do projeto:

```bash
docker-compose up -d
```

Isso inicia um container MySQL com:

- Banco: `plataforma_ideias`
- Usuário: `grupo4`
- Senha: `123456`

> 💡 Se já tiver rodado o projeto antes e o schema do banco estiver desatualizado, use `docker-compose down -v && docker-compose up -d` para recriar o banco do zero.

### 3. Executar o backend

```bash
cd backend
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### 4. Executar o frontend

Em outro terminal:

```bash
cd frontend
npm install
ng serve
```

A aplicação ficará disponível em `http://localhost:4200`.

---

## 🧪 Rodando os testes

### Backend (JUnit)

```bash
cd backend
./mvnw test
```

### Frontend (Vitest)

```bash
cd frontend
ng test --watch=false
```

---

## ⚙️ Configuração do banco

O backend utiliza as seguintes configurações (definidas em `application.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plataforma_ideias
spring.datasource.username=grupo4
spring.datasource.password=123456
```

---

## 🚧 Status do Projeto

Em desenvolvimento — Terceira Iteração
