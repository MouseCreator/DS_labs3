
FROM maven:3.8.4-openjdk-17 AS build

COPY src /home/app/src
COPY pom.xml /home/app


RUN mvn -f /home/app/pom.xml clean package


FROM openjdk:17


COPY --from=build /home/app/target/*.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]