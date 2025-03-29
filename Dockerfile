FROM openjdk:21

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} rental-service.jar

ENTRYPOINT ["java","-jar","rental-service.jar"]

EXPOSE 8080