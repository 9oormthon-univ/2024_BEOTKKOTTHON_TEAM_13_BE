FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE prod
ENV CONFIG_HOST localhost:3964
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dapp.config-host=${CONFIG_HOST}", "-jar","/app.jar"]
