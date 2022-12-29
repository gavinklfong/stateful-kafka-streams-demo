# Spring Boot Kafka Demo 

## Overview
This is to demonstrate the use of Kafka client library and Spring Kafka. The sample application is a message driven 
insurance claim review process, it consumes claim requests from topic "claim-submitted" and output the review result
to another topic "claim-update". 

It aims at covering a basic usage of message producer and consumer for anyone to start picking up the knowledge.

![Application Component Diagram](https://github.com/gavinklfong/spring-kafka-demo/blob/master/blob/Sample_App.jpg?raw=true)

## Build & Run

Your local environment should have Java 17, maven and docker installed in order to build and run this applcation.

First of all, start up a Kafka in docker container:
```
docker run -e ADV_HOST=127.0.0.1 -e SAMPLEDATA=0 -e RUNTESTS=0 -p 3030:3030 -p 9092:9092 \
--rm landoop/fast-data-dev:latest
```

As the docker container is up and running, you should be able to see the web UI when accessing to http://localhost:3030 

Run this command to build and run the application

```
mvn spring-boot:run
```

## Manual Test

Look at the source code of `ClaimsRequestController` which shows you how to produce Kafka messages using 
Kafka client library and Spring `KafkaTemplate` respectively:

Run this command to produce a new insurance claim request using kafka client library:
```
curl --request POST 'http://localhost:8080/claim-1'
```

This command produce a new instance claim request using Spring `KafkaTemplate`:
```
curl --request POST 'http://localhost:8080/claim-2'
```

Once new instance claim requests are generated, topics will appear on this web UI (http://localhost:3030/kafka-topics-ui/#/)
and you will be able to see the generated requests on topic "claim-submitted" and the review result on topic "claim-updated"

Run this command to retrieve the output messages from topic "claim-updated":
```
curl --request GET 'http://localhost:8080/claim-results'
```

