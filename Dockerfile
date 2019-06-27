FROM maven:3.6.1-slim as builder
COPY . /app
WORKDIR /app
RUN mvn -B install

FROM openjdk:8u212-jre
COPY --from=builder /app/target/sdk-generator.jar /
ENTRYPOINT ["java", "-jar", "/sdk-generator.jar"]
