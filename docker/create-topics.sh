docker exec kafka \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server localhost:9092 \
--topic transactions --create --partitions 3 --replication-factor 1

docker exec kafka \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server localhost:9092 \
--topic account-balances --create --partitions 3 --replication-factor 1

docker exec kafka \
/opt/lensesio/kafka/bin/kafka-topics --bootstrap-server localhost:9092 \
--topic __redpanda.connectors_logs --create --partitions 3 --replication-factor 1
