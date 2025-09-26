FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/eurika-service-0.0.1.jar eureka-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "eureka-service.jar"]
