# Java 21 (LTS) - official Eclipse Temurin image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cache layer)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Expose port (Render injects PORT)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]