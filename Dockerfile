# Etapa 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia arquivos de dependências primeiro para cache de build
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila e empacota o projeto sem rodar os testes
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o jar do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Porta exposta pelo Spring Boot
EXPOSE 8080

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
