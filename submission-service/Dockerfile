FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/submission-service-0.0.1-SNAPSHOT.jar SubmissionService.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","SubmissionService.jar"]
