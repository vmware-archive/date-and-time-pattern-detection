# Define base docker image
FROM openjdk:17
LABEL maintainer="kirilraykov@gmail.com"
COPY ./rest-api-service/target/*.jar date-time-pattern-detection-service.jar
ENTRYPOINT ["java","-jar","date-time-pattern-detection-service.jar"]
