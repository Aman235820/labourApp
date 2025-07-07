FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/labourApp-0.0.1-SNAPSHOT.jar labourappdocker.jar
ENTRYPOINT ["java","-jar","/labourappdocker.jar"]