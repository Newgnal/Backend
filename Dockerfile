FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/firebase ./firebase
ENTRYPOINT [ "java", "-jar",  "/app.jar" ]
