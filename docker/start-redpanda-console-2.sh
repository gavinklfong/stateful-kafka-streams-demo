docker run --network=host \
-p 8080:8080 \
-v ./redpanda-console-config.yaml:/tmp/config/redpanda-console-config.yaml \
-e CONFIG_FILEPATH=/tmp/config/redpanda-console-config.yaml \
--rm \
docker.redpanda.com/redpandadata/console:latest