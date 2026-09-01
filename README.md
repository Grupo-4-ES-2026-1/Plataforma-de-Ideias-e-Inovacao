# 💡 Plataforma de Ideias e Inovação

Um espaço para a comunidade da UFAPE propor, discutir, votar e acompanhar ideias de melhoria para a universidade.

## :octocat: Integrantes

| | | | | |
|:---:|:---:|:---:|:---:|:---:|
| [Laissa Gama](https://github.com/laissagamma) | [Mário Ramon](https://github.com/joyeuxpierrot) | [Pedro Duarte](https://github.com/PedrokaIsACoder) | [José Jonathan](https://github.com/jonathanbraga47) | [Antonio Victor](https://github.com/vieiraAnttonio) |

## 📃 Sobre o Projeto

Projeto desenvolvido para a disciplina de **Engenharia de Software**, ministrada pela Professora [Thaís Alves Burity Rocha](https://github.com/taburity), da UFAPE (campus Garanhuns), referente à 2ª Verificação de Aprendizagem.

O sistema se propõe a catalogar as ideias da comunidade para melhorar a UFAPE, oferecendo um espaço centralizado onde estudantes, docentes e servidores podem propor melhorias, votar nas mais relevantes, acompanhar seu andamento até a implantação e, para quem avalia e gerencia as propostas, contar com um ranking de apoio da comunidade e um dashboard de indicadores.

## 📍 Objetivos

O usuário deve ser capaz de se cadastrar e autenticar no sistema, cadastrar propostas de melhoria para a universidade e consultar as propostas já existentes, filtrando e ordenando conforme necessário. Cada proposta possui um status que evolui ao longo do tempo (submetida, em análise, aprovada, em implantação, implantada ou rejeitada), com histórico completo de todas as mudanças. Usuários comuns podem votar nas propostas ainda em avaliação, enquanto usuários responsáveis pela avaliação e gestão das propostas (ADMIN) podem alterar o status de uma proposta, consultar o ranking das mais votadas e acompanhar indicadores consolidados num dashboard.

## 👤 Perfis de acesso

| Ação | USER | ADMIN |
|---|:---:|:---:|
| Cadastrar, listar e consultar propostas | ✅ | ✅ |
| Votar em propostas (SUBMETIDA / EM_ANALISE) | ✅ | ❌ |
| Consultar histórico de status de uma proposta | ✅ | ✅ |
| Alterar o status de uma proposta | ❌ | ✅ |
| Consultar o Ranking de propostas mais votadas | ❌ | ✅ |
| Consultar o Dashboard de indicadores | ❌ | ✅ |
| Gerenciar usuários (listar / excluir) | ❌ | ✅ |

> A restrição de votação para ADMIN existe para evitar conflito de interesse: quem decide o status de uma proposta não deveria também influenciar sua votação.

## ✨ Funcionalidades já implementadas

- 🔐 **Autenticação e autorização** completas com JWT (cadastro, login e logout)
- 👤 Controle de acesso por perfil (**USER** / **ADMIN**), aplicado tanto no frontend (Guards) quanto no backend (Spring Security)
- 📝 **Cadastro de propostas**, vinculado ao usuário autenticado
- 📋 **Listagem, filtro por status e ordenação** de propostas
- 🔄 **Atualização de status** da proposta (SUBMETIDA → EM_ANALISE → APROVADA/REJEITADA → EM_IMPLANTACAO → IMPLANTADA), restrita a ADMIN, com validação das transições permitidas
- 🕘 **Histórico de status**, com data de cada mudança
- 🗳️ **Votação** em propostas ainda em avaliação, restrita a usuários comuns (não-ADMIN)
- 🏆 **Ranking de propostas** mais votadas, com filtro por categoria e período, restrito a ADMIN
- 📊 **Dashboard de indicadores**: total de propostas, distribuição por status, taxa de aprovação e engajamento da comunidade, com filtros, restrito a ADMIN
- 🛡️ Rotas protegidas no frontend (Guards) e no backend (Spring Security + Filter JWT)
- ✅ Testes automatizados (JUnit no backend, Vitest no frontend)
- 🔗 **Integração CI/CD** via GitHub Actions (build, testes, cobertura JaCoCo, análise SonarCloud)
- 🐳 **Deploy containerizado** (Docker) em produção, no Render

## 🛠️ Tecnologias Usadas

### Frontend
- [Angular 22](https://angular.dev/)
- JWT para autenticação, com interceptor HTTP e rotas protegidas por Guard

### Backend
- [Java 21](https://www.java.com/)
- [Spring Boot 4.1](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security) + JWT
- [Maven](https://maven.apache.org/)

### Banco de Dados
- **Produção:** PostgreSQL (Render)
- **Desenvolvimento local:** MySQL 8.4 (via Docker)
- **Testes automatizados:** H2 (em memória)

### Testes e Qualidade
- [JUnit 5](https://junit.org/junit5/) + Mockito + AssertJ (backend)
- [Vitest](https://vitest.dev/) (frontend)
- [JaCoCo](https://www.jacoco.org/jacoco/) — cobertura de testes do backend
- [SonarCloud](https://sonarcloud.io/) — análise de qualidade e segurança de código

### CI/CD e Infraestrutura
- **GitHub Actions** — build, testes e análise de qualidade automatizados a cada push/PR na `main`
- **Docker** — containerização do frontend (Nginx) e do backend (Spring Boot)
- **Render** — hospedagem do frontend, backend e banco PostgreSQL de produção

### Gerenciamento
- GitHub Projects (Quadro Scrum)

---

## 🚀 Como executar o projeto localmente

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

O relatório de cobertura (JaCoCo) é gerado em `backend/target/site/jacoco/index.html`.

### Frontend (Vitest)

```bash
cd frontend
ng test --watch=false
```

---

## ⚙️ Configuração do banco (ambiente local)

O backend utiliza as seguintes configurações por padrão (definidas em `application.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plataforma_ideias
spring.datasource.username=grupo4
spring.datasource.password=123456
```

Os perfis `test` (H2) e `prod` (PostgreSQL) têm suas próprias configurações em `application-test.properties` e `application-prod.properties`, respectivamente.

---

## 🌐 Aplicação em produção (Render)

- **Frontend:** https://plataforma-de-ideias-e-inovacao-1.onrender.com
- **Backend (API REST):** https://plataforma-de-ideias-e-inovacao.onrender.com

> ⚠️ Ambos usam o plano gratuito do Render, que "dorme" após um período de inatividade. A primeira requisição após um tempo parado pode levar até ~50s para responder.

## 🚧 Status do Projeto

Finalizado
