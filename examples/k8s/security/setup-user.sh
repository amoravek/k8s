#!/usr/bin/env bash

export KUBE_USER=${1,,}
export KUBE_NS=${2:-$KUBE_USER}

if [[ -z $KUBE_USER || -z $KUBE_NS ]]; then
    echo "Usage: $0 <KUBE_USER> [<KUBE_NS>]"
    exit 1
fi

# RBAC
kubectl create namespace ${KUBE_USER} || true
kubectl create rolebinding ${KUBE_USER}-${KUBE_USER}-admin --clusterrole admin --user ${KUBE_USER} --namespace ${KUBE_NS}

# ResourceQuota, LimirRange
kubectl create -f manifests/resource-quota.yaml -n ${KUBE_USER} --dry-run=client -o yaml | kubectl apply -f -
kubectl create -f manifests/limit-range.yaml -n ${KUBE_USER} --dry-run=client -o yaml | kubectl apply -f -
