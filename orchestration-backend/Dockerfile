FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/orchestration-backend-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
