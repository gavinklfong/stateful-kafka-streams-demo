#!/bin/bash

BASEDIR=$(dirname "$0")

docker compose down
docker compose up -d

sleep 3

${BASEDIR}/s3-create-bucket.sh
${BASEDIR}/create-topic.sh
