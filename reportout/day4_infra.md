
# Day 4 Infra

## Day4 - creating infra
* Refactored `interchange.tf` to `interchange-module/main.tf`, `.../variables.tf`, and `.../outputs.tf`
* Expose EKS endpoint and Kinesis stream ARN in output of terraforming
* Make sure to replace the `host` line in the `kubernetes/backend-ingress.yml` with the correct EKS hostname
 