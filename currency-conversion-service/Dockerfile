FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
COPY target/currency-conversion-service-1.0.jar app.jar