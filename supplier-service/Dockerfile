FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/target/supplier-service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 50051
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

