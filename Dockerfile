FROM selenium/standalone-chrome:114.0

# 빌드된 jar 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver
ENV GOOGLE_CHROME_PATH=/usr/bin/google-chrome

ENTRYPOINT ["java", "-jar", "/app.jar"]
