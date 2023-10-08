# Kafka S3 Sink Connector & Source Connector Exercise

## Overview
This is to demonstrate the use of S3 connectors. The shopping-order-producer randomly generates shopping orders
to a Kafka topic `shopping-orders` every 0.5 sec

Here is the exercise:
* Set up S3 sink connector that sinks the topic into S3 bucket `shopping-orders`
* Set up S3 source connector that sync data from the same S3 bucket to another topic `shopping-orders-copy`

## How to start local environment

Run this command to start the local environment in docker containers:
```
./docker/start-cluster.sh
```

Kafka and LocalStack will be started. You can access the Kafka using [Web UI](http://localhost:3030)

LocalStack is to run S3 bucket on local environment.

Next, run the shopping order producer
```
docker run --rm --net=host whalebig27/shopping-order-producer:latest
```

Then, you will see the messages published into topic `shopping-orders`

Connector properties files and useful commands can be found in `./docker` folder