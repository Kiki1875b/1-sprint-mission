#!/bin/bash

set -e

echo "LocalStack 실행 중..."
docker compose -f docker-compose.local.yml up -d localstack

echo "LocalStack 준비 중..."
sleep 5

echo "테스트 실행"
./gradlew test  --tests "com.sprint.mission.discodeit.storage.S3BinaryContentStorageTest"

echo "정리 중"
docker compose -f docker-compose.local.yml down
