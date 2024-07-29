FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE prod
ENV APPLICATION_PORT 3743
ENV SERVER_CONFIG_URL http://localhost:3964
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dserver.port=${APPLICATION_PORT}", "-Dapp.server-config.url=${SERVER_CONFIG_URL}", "-jar","/app.jar"]
