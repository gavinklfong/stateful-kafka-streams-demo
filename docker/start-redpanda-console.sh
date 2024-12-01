docker run --network=host \
-p 8080:8080 \
-e KAFKA_BROKERS=localhost:9092 \
-e KAFKA_SCHEMAREGISTRY_ENABLED=TRUE \
-e KAFKA_SCHEMAREGISTRY_URLS=http://localhost:8081 \
--rm \
docker.redpanda.com/redpandadata/console:latest