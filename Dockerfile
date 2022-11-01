# Define base docker image
FROM openjdk:17
LABEL maintainer="kraykov@vmware.com"
ARG JAR_FILE=rest-api-service/target/*.jar
COPY ${JAR_FILE} date-time-pattern-detection-service.jar
ENTRYPOINT ["java","-jar","/date-time-pattern-detection-service.jar"]
