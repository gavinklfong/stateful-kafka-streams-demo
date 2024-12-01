docker run --name mysql \
-e MYSQL_ROOT_PASSWORD=Password!@ \
-p 3306:3306 \
-v ./mysql/schema.sql:/docker-entrypoint-initdb.d/1.sql \
--rm \
mysql:8