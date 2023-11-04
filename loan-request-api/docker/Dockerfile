FROM openjdk:17-jdk-slim-buster

ARG JAR_FILE=app/target/*.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080

WORKDIR /app

COPY docker/entrypoint.sh /app/entrypoint.sh

CMD ["/app/entrypoint.sh"]

