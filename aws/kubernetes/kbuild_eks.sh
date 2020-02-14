#!/usr/bin/env bash
CLUSTER_IP=$(cd ../interchange-module;terraform output cluster_ip)
CLUSTER_IP=${CLUSTER_IP,,}
echo "Eventually we will use $CLUSTER_IP"
sed "s|{{eks_hostname}}|${CLUSTER_IP}|g" eks/custom.json > eks/customTarget.json
kubectl kustomize eks
