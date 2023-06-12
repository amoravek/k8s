#!/usr/bin/env bash

export KUBE_USER=${1,,}
export KUBE_GROUP=${2,,}
export KUBE_NS=${3:-$KUBE_USER}

EXPIRATION_SECS=31536000 # 1 year

if [[ -z $KUBE_USER || -z $KUBE_GROUP ]]; then
    echo "Usage: $0 <KUBE_USER> <KUBE_GROUP> [<KUBE_NS>]"
    exit 1
fi

openssl genrsa -out ${KUBE_USER}-key.pem 2048
openssl req -new -key ${KUBE_USER}-key.pem -out ${KUBE_USER}-csr.pem -subj "/CN=${KUBE_USER}/O=${KUBE_GROUP,,}"

CSR_BASE64="$(cat ${KUBE_USER}-csr.pem | base64 | tr -d '\n')"

cat <<EOF | kubectl apply -f -
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: ${KUBE_USER}
spec:
  request: ${CSR_BASE64}
  signerName: kubernetes.io/kube-apiserver-client
  expirationSeconds: ${EXPIRATION_SECS}
  usages:
  - client auth
EOF

# PKI
kubectl certificate approve ${KUBE_USER}
kubectl get csr/${KUBE_USER} -o jsonpath='{ .status.certificate }' | base64 -d > ${KUBE_USER}-crt.pem

# RBAC
kubectl create namespace ${KUBE_USER} || true
kubectl create rolebinding ${KUBE_USER}-${KUBE_USER}-admin --clusterrole admin --user ${KUBE_USER} --namespace ${KUBE_NS}

# ResourceQuota, LimirRange
kubectl create -f manifests/resource-quota.yaml -n ${KUBE_USER} --dry-run=client -o yaml | kubectl apply -f -
kubectl create -f manifests/limit-range.yaml -n ${KUBE_USER} --dry-run=client -o yaml | kubectl apply -f -

export USER_KEY="$(cat ${KUBE_USER}-key.pem | base64 | tr -d '\n')"
export USER_CERT="$(cat ${KUBE_USER}-crt.pem | base64 | tr -d '\n')"

envsubst < kubeconfig-template > config.${KUBE_USER}
echo "KUBECONFIG for ${KUBE_USER} written to config.${KUBE_USER}"
echo "=== OK ==="
