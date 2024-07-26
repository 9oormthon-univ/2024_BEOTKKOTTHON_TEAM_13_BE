FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE prod
ENV APPLICATION_PORT 925
ENV CONFIG_SERVER_URL http://localhost:3964
ENV EUREKA_URL http://localhost:3743/eureka/
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dapp.config-server.url=${CONFIG_SERVER_URL}", "-Dapp.eureka-server.url=${EUREKA_URL}", "-jar","/app.jar"]