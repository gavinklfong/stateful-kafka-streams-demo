docker run --name kafka \
-e ADV_HOST=127.0.0.1 \
-e SAMPLEDATA=0 \
-e RUNTESTS=0 \
-p 3030:3030 -p 9092:9092 -p 8081:8081 -p 8083:8083 \
--rm \
landoop/fast-data-dev:latest