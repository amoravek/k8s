#!/usr/bin/env bash

set -e

# Source the environment file
if [ -f /vault/secrets/env-vars.sh ]; then
    . /vault/secrets/env-vars.sh
fi

# Start the application
exec java \
-cp \
"/app/resources:/app/classes:/app/libs/*" \
"k8s.sample.KubeSampleApp" \
server
