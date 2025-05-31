FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY lot-service/pom.xml ./lot-service/pom.xml
COPY lot-service/src ./lot-service/src

RUN mvn -f lot-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=builder /app/lot-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]