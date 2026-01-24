# Java 17 for Spring Boot 3.x
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy everything
COPY . .

# Build the app (works with or without mvnw)
RUN ./mvnw clean package || mvn clean package

# Spring Boot default port
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/*.jar"]
