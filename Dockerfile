FROM maven:3.9.3-amazoncorretto-17 AS build

WORKDIR /usr/src/app

COPY pom.xml .

COPY application ./application
COPY domain ./domain
COPY shared ./shared
COPY web ./web
COPY infrastructure ./infrastructure

RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

FROM openjdk:19-alpine

WORKDIR /usr/app

COPY --from=build /usr/src/app/web/target/web-0.0.1-SNAPSHOT.jar /usr/app

EXPOSE 8080

CMD ["java", "-jar", "web-0.0.1-SNAPSHOT.jar"]
