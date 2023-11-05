# Demonstration of Stateful Kafka Streams Data Processing

## Overview
This is to demonstrate the implementation of stateful data stream processing using Kafka Streams. 

The transaction-producer generates transactions every 1 second to a Kafka topic `transactions` and a topology
calculate account balance in a real time based on the previous balance.

The following modules give you the idea how to build stateless and stateful data stream processing:
- **transaction-filter-topology**: Stateless topology of transaction filter that output only transfer transaction
- **account-balance-calculation-topology**: Stateful topology of account balance calculation using Kafka key-value state store
- **loan-request-api**: A simple REST API that publishes loan requests to a Kafka topic
- **loan-evaluation-service**: Loan evaluation service is driven by Kafka topic and retrieves the materialized account balances in a local data store for loan evaluation
- **loan-evaluation-topology**: Loan evaluation topology that materializes account balances into KTable and join with loan requests for loan evaluation

## How to start local environment

Run this command to start the local environment in docker containers:
```
cd docker
docker compose up
```

Kafka will be started. You can access the Kafka using [Web UI](http://localhost:3030)

Next, run the transaction producer
```
docker run --rm --net=host whalebig27/transaction-producer:latest
```
Then, you will see the messages published into topic `transactions`

**Run Transaction Filter Topology**
Start up the topology and check the output on topic `transfer-transactions`
```
docker run --rm --net=host whalebig27/transaction-filter-topology:latest
```

**Run Account Balance Calculation Topology**
Start up the topology and check the output on topic `account-balances`
```
docker run --rm --net=host whalebig27/account-balance-calculation-topology:latest
```

**Run Loan Request API**
Start up an API. Expose an endpoint `POST /loans` which publishes loan request to Kafka topic `loan-requests` 
```
docker run --rm --net=host whalebig27/loan-request-api:latest
```

**Run Loan Evaluation Service**
Step 1 - Firstly setup sink connector that materialize Kafka topic `account-balances` into a MySQL table `loan.account_balance`. 
Use the sink connector properties file in `docker/account-balances-mysql-sink-connector.properties`

Step 2 -
```
docker run --rm --net=host whalebig27/loan-evaluation-service:latest
```

**Run Loan Evaluation Topology**
```
docker run --rm --net=host whalebig27/loan-evaluation-topology:latest
```