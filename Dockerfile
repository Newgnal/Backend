FROM openjdk:17-jdk-slim

#chromeDriver 설치
RUN apt-get update && \
    apt-get install -y wget unzip gnupg && \
    wget -N https://chromedriver.storage.googleapis.com/124.0.6367.91/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
    rm chromedriver_linux64.zip
    
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar",  "/app.jar" ]
