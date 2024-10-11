FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/eureka_server-0.0.1-SNAPSHOT.jar EurekaServer.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","EurekaServer.jar"]
