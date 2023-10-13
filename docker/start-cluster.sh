#!/bin/bash

BASEDIR=$(dirname "$0")

docker compose down
docker compose up -d

sleep 3

${BASEDIR}/create-topics.sh
