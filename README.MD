# Demonstration of Stateful Kafka Streams Data Processing

## Overview
This is to demonstrate the implementation of stateful data stream processing using Kafka Streams. 
The transaction-producer generates transactions every 1 second to a Kafka topic `transactions` and a topology
calculate account balance in a real time based on the previous balance.

These 2 modules give you the idea how to build stateless and stateful data stream processing:
- **transaction-filter-topology**: Stateless topology of transaction filter that output only transfer transaction
- **account-balance-calculation-topology**: Stateful topology of account balance calculation using Kafka key-value state store 

## How to start local environment

Run this command to start the local environment in docker containers:
```
docker run -e ADV_HOST=127.0.0.1 -e SAMPLEDATA=0 -e RUNTESTS=0 -p 3030:3030 -p 9092:9092 --rm landoop/fast-data-dev:latest
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
