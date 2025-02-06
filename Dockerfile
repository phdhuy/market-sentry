FROM maven:3.9.3-amazoncorretto-17 AS build

WORKDIR /usr/src/app

COPY pom.xml .
COPY application/pom.xml ./application/
COPY domain/pom.xml ./domain/
COPY shared/pom.xml ./shared/
COPY web/pom.xml ./web/
COPY infrastructure/pom.xml ./infrastructure/

RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:19-alpine


RUN apk update && apk add --no-cache \
    firefox \
    xvfb \
    dbus \
    ttf-freefont \
    fontconfig \
    libx11 \
    libxrender \
    libxext \
    libxtst \
    libxdamage \
    libxcomposite \
    libxrandr \
    alsa-lib \
    libgcc \
    libstdc++ \
    mesa-dri-gallium

RUN wget -q https://github.com/mozilla/geckodriver/releases/download/v0.31.0/geckodriver-v0.31.0-linux64.tar.gz \
    && tar -xvzf geckodriver-v0.31.0-linux64.tar.gz \
    && mv geckodriver /usr/local/bin/ \
    && chmod +x /usr/local/bin/geckodriver \
    && rm geckodriver-v0.31.0-linux64.tar.gz

WORKDIR /usr/app

COPY --from=build /usr/src/app/web/target/web-0.0.1-SNAPSHOT.jar /usr/app

EXPOSE 8081

CMD ["java", "-jar", "--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED", "web-0.0.1-SNAPSHOT.jar"]
