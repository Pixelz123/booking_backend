# ---------- Stage 1: Build the application ----------
FROM gradle:8.14.2-jdk17 AS builder

WORKDIR /app

# Copy all project files
COPY . .

# Build the project without running tests
RUN gradle clean build -x test


# ---------- Stage 2: Create a lightweight runtime image ----------
FROM eclipse-temurin:17-jdk-alpine

# Update Alpine packages to reduce known CVEs
RUN apk --no-cache upgrade

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar


# Expose the application's port
EXPOSE 8080


# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
