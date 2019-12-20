# Infra - report out Day 2
## Introduce terraform to make infra-creation repeatable

* Installed Terraform CLI (https://www.terraform.io/)

* Created `interchange.tf` and `variables.tf`
* Implement using `terraform apply` (repeatedly, will process changes)
    * Need at least 3 or 4 nodes, to get enough room for k8s system pods
    * Teardown can be done with `terraform destroy`
* Try deploying a hello-world pod
    * `kubectl apply -f ./tryout/tryout-service.yml`
    * `kubectl apply -f ./tryout/tryout-deployment.yml`
* Find the external address for our cluster
    * `kubectl describe service tryout-service | grep Ingress`
* check to see if you see the Nginx default page on the reported url

* You can run a GUI dashboard (see `../aws/README.md`)

## Setup Registry
* Follow AWS instructions/steps (see https://eu-central-1.console.aws.amazon.com/ecr/repositories?region=eu-central-1)