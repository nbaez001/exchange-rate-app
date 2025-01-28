FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
COPY target/currency-exchange-service-1.0.jar app.jar