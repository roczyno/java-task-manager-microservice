FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/config-server-0.0.1-SNAPSHOT.jar ConfigServer.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","ConfigServer.jar"]
