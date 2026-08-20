# Etapa 1: build da aplicação com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia primeiro só o pom.xml para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código-fonte e builda
COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa 2: imagem final, só com o JRE e o jar gerado
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Perfil de produção é definido via variável de ambiente SPRING_PROFILES_ACTIVE no Render
ENTRYPOINT ["java", "-jar", "app.jar"]
