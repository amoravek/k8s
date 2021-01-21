#!/usr/bin/env bash

docker build -t k8s-sample-app:1.0 .
docker tag k8s-sample-app:1.0 harbor.trask.cz/arm64/k8s-sample-app:1.0
docker push harbor.trask.cz/arm64/k8s-sample-app:1.0

