# Kubernetes operations

## Setup
* make sure the Terraform stuff is finished (see `../interchange-module`)
* make sure your CLI is configured to work with the EKS cluster (see `../README.md`)

## Running the commands
We use Kustomize to inject Terraform results into the kubernetes yamls.

Usage: see `kbuild_eks.sh`
