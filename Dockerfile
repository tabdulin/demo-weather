FROM maven:3.6-jdk-11-slim as builder

RUN mkdir demo
WORKDIR /demo
COPY pom.xml .
RUN mvn verify --fail-never
COPY src ./src
RUN mvn clean package

FROM openjdk:11-jre-slim
RUN mkdir demo
WORKDIR /demo
COPY --from=builder /demo/target/demo-weather.jar .
ENTRYPOINT ["java", "-jar", "weather.jar"]