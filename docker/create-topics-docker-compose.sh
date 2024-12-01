docker run \
--rm \
--network docker_default \
landoop/fast-data-dev:latest \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server kafka:9092 \
--topic transactions --create --partitions 3 --replication-factor 1

docker run \
--rm \
--network docker_default \
landoop/fast-data-dev:latest \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server kafka:9092 \
--topic account-balances --create --partitions 3 --replication-factor 1

docker run \
--rm \
--network docker_default \
landoop/fast-data-dev:latest \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server kafka:9092 \
--topic __redpanda.connectors_logs --create --partitions 3 --replication-factor 1
