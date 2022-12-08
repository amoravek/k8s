#!/usr/bin/env bash

export KUBE_USER=${1,,}
export KUBE_NS=${2:-$KUBE_USER}

if [[ -z $KUBE_USER ]]; then
    echo "Usage: $0 <KUBE_USER> [<KUBE_NS>]"
    exit 1
fi

kubectl delete rolebinding -n ${KUBE_USER} ${KUBE_USER}-${KUBE_NS,,}-admin
