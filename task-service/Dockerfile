FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/task-service-0.0.1-SNAPSHOT.jar TaskService.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","TaskService.jar"]
