#!/bin/bash

echo "[INFO] : Downloading encrypted env file..."
aws s3 cp s3://discodeit-binary-content-storage-hjw/discodeit.env.encrypted /tmp/discodeit.env.encrypted

echo "[INFO] : Decrypting..."
aws kms decrypt --ciphertext-blob fileb:///tmp/discodeit.env.encrypted --output text --query Plaintext | base64 -d > /tmp/discodeit.env

echo "[INFO] : Exporting to environment variables..."
set -a
. /tmp/discodeit.env
set +a

echo "[INFO] JVM_OPTS: $JVM_OPTS"
echo "[INFO] APP_OPTS: $APP_OPTS"
echo "[INFO] Running: java $JVM_OPTS -jar app.jar $APP_OPTS"
echo "[INFO] Starting application..."
exec java $JVM_OPTS -jar app.jar $APP_OPTS
