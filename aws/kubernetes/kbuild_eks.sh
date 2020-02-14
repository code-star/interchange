#!/usr/bin/env bash
CLUSTER_IP=$(cd ../interchange-module;terraform output cluster_ip)
CLUSTER_IP=${CLUSTER_IP,,}
echo "Eventually we will use the hostname from $CLUSTER_IP"
EKS_HOST=${CLUSTER_IP#"https://"}
sed "s|{{eks_hostname}}|${EKS_HOST}|g" eks/custom.json > eks/customTarget.json
kubectl kustomize eks > ../target/build.yaml
kubectl apply -f ../target/build.yaml
