FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE prod
ENV SERVER_CONFIG_URL http://localhost:3964
ENV SERVER_EUREKA_URL http://localhost:3743/eureka/
ENV APPLICATION_PORT 3615
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dapp.server-config.url=${SERVER_CONFIG_URL}", "-Dapp.server-eureka.url=${SERVER_EUREKA_URL}", "-Dserver.port=${APPLICATION_PORT}", "-jar","/app.jar"]