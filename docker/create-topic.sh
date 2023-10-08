docker run --rm --net=host landoop/fast-data-dev:latest kafka-topics \
--create                    \
--bootstrap-server localhost:9092  \
--replication-factor 1      \
--partitions 3               \
--topic shopping-orders

docker run --rm --net=host landoop/fast-data-dev:latest kafka-topics \
--create                    \
--bootstrap-server localhost:9092  \
--replication-factor 1      \
--partitions 3               \
--topic shopping-orders-copy