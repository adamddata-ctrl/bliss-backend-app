# Stage 1: Build the Maven source code into a JAR file inside the cloud
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime environment to run the built JAR
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /target/*.jar app.jar

# Explicitly forward the environment variables to the Java process at runtime
ENTRYPOINT java -Dspring.datasource.url=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME -Dspring.datasource.username=$DB_USER -Dspring.datasource.password=$DB_PASSWORD -jar /app.jar
