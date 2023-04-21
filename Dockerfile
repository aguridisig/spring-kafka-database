FROM openjdk:17-jdk-alpine
MAINTAINER tuprima.com
COPY target/spring-kafka-database-0.0.1.jar spring-kafka-database-0.0.1.jar
ENTRYPOINT ["java","-jar","/spring-kafka-database-0.0.1.jar"]