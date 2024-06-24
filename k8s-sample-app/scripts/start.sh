#!/usr/bin/env sh

set -e

# Source the environment file
if [ -f /vault/secrets/env-vars.sh ]; then
    . /vault/secrets/env-vars.sh
fi

# Start the application
exec java \
-javaagent:/agent/jmx_prometheus_javaagent-0.19.0.jar=12345:/agent/config.yaml \
-cp \
"/app/resources:/app/classes:/app/libs/*" \
"k8s.sample.KubeSampleApp" \
server
