FROM openjdk:17-alpine3.14 as build

COPY . .

RUN sed -i 's/\r$//' gradlew

RUN ./gradlew bootJar -x test

FROM openjdk:17-alpine3.14

COPY --from=build config/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]