#!/usr/bin/env bash
CLUSTER_IP=$(terraform output cluster_ip)
CLUSTER_IP=${CLUSTER_IP,,}
echo $CLUSTER_IP
kubectl apply -f kubernetes/backend-ingress.yml
