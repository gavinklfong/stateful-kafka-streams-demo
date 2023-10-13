docker run --rm --net=host landoop/fast-data-dev:latest kafka-topics \
--create                    \
--bootstrap-server localhost:9092  \
--replication-factor 1      \
--partitions 1               \
--topic transactions

docker run --rm --net=host landoop/fast-data-dev:latest kafka-topics \
--create                    \
--bootstrap-server localhost:9092  \
--replication-factor 1      \
--partitions 1               \
--topic transfer-transactions

docker run --rm --net=host landoop/fast-data-dev:latest kafka-topics \
--create                    \
--bootstrap-server localhost:9092  \
--replication-factor 1      \
--partitions 1               \
--topic account-balances