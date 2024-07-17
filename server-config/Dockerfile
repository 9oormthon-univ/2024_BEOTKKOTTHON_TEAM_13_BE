FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE prod
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.config.location=classpath:/application.yml,/configs/application.yml", "-jar","/app.jar"]