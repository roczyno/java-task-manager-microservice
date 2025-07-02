FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar UserService.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","UserService.jar"]
