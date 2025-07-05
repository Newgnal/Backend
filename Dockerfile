FROM openjdk:17-jdk-slim

# 필수 패키지 설치
RUN apt-get update && apt-get install -y \
    wget unzip gnupg curl ca-certificates \
    fonts-liberation libappindicator3-1 libasound2 libatk-bridge2.0-0 \
    libatk1.0-0 libcups2 libdbus-1-3 libdrm2 libgbm1 libgtk-3-0 \
    libnspr4 libnss3 libx11-xcb1 libxcomposite1 libxdamage1 libxrandr2 xdg-utils \
    --no-install-recommends

# Google Chrome 138 버전 직접 설치
RUN wget https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/138.0.7204.92/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip && \
    mv chrome-linux64 /opt/chrome && \
    ln -s /opt/chrome/chrome /usr/bin/google-chrome && \
    rm chrome-linux64.zip

# ChromeDriver 138 설치
RUN wget https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/138.0.7204.92/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip && \
    mv chromedriver-linux64/chromedriver /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
    rm -rf chromedriver-linux64.zip chromedriver-linux64

# 빌드된 jar 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver
ENV GOOGLE_CHROME_PATH=/usr/bin/google-chrome

ENTRYPOINT ["java", "-jar", "/app.jar"]
