FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/receiptprocessor-*.jar /app/receiptprocessor.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/receiptprocessor.jar"]
