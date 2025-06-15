# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

# Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/project-query/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]