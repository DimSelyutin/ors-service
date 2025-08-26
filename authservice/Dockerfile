FROM openjdk:17  as build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' mvnw

RUN ./mvnw clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build app/config/target/*.jar authservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "authservice.jar"]