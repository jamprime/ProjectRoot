# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

# Run stage for project-command
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/project-command/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 