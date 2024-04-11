#! /usr/bin/env bash

# shellcheck disable=SC2164
cd app

docker-compose down
docker-compose up -docker

echo "Build complete"