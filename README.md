# spring-kafka-database
This project is a simple example of how to use Kafka with Spring Boot and a database.
In this case, the database is MySQL, but it could be any relational database.
There are two Spring Boot applications in this project. The first one expose a RestApi and has an endpoint that call to
the kafka  producer that sends messages to a Kafka topic. This application also has a consumer that reads messages from the Kafka topic and saves them to a database.
The second one is a consumer that reads messages from the Kafka topic and send notifications via email.
Also, there is an Angular application that consumes the RestApi and shows the users saved in the database in a table and has a form to create new users.

## Services
The docker-compose file runs the following services:
- RestApi
http://localhost:8080/swagger-ui/index.html#/
- Kafa UI´s
http://localhost:8085/ Kafdrop
http://localhost:8088/topics
- Prometheus
http://localhost:9090 
- Grafana
http://localhost:3000 
- WSO2 API Manager
https://localhost:9443/publisher/apis/

## Requirements
To run the project, you need to have installed Docker and Docker Compose.
Then, you need to run the following command:
docker-compose -f docker-compose.yml up -d