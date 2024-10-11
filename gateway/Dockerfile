FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/gateway-0.0.1-SNAPSHOT.jar Gateway.jar
EXPOSE 5001
ENTRYPOINT ["java","-jar","Gateway.jar"]
