# -------- Build Stage --------
FROM maven:3.9.6-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# -------- Run Stage --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/labourApp-0.0.1-SNAPSHOT.jar labourappdocker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/labourappdocker.jar"]
