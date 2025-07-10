# ---------- Stage 1: Build the application ----------
FROM gradle:8.14.2-jdk17 AS builder

WORKDIR /app

# Copy all project files
COPY . .

# Build the project without running tests
RUN gradle clean build -x test


# ---------- Stage 2: Create a stable runtime image ----------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
