# Define base docker image
FROM openjdk:17
LABEL maintainer="kirilraykov@gmail.com"
COPY ./target/*.jar date-time-pattern-detection-service.jar
ENTRYPOINT ["java","-jar","date-time-pattern-detection-service.jar"]
