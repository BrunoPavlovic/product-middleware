FROM openjdk:21-jdk-slim
WORKDIR /app

COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradlew.bat .
COPY gradle ./gradle

COPY src ./src

COPY build/libs/middleware-0.0.1-SNAPSHOT.jar middleware.jar

EXPOSE 8080
CMD ["java", "-jar", "middleware.jar"]