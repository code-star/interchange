
# Day 5 Infra

## Day5 - tying Terraform to Kubectl
* Started using Kustomize to generate kubectl files out of base config and a (generated) dynamic config extracted from Terraform results
    * Replaces the `host` line in the `kubernetes/backend-ingress.yml` with the correct EKS hostname

* Duplication (to be removed? or refactored to work with a single source of truth)
    * Terraform performs 'initial' deployment of our application pods (main.tf)
    * K8S yamls also capable of performing these deployments (kubernetes/base/*.yaml)
    * Our CI/CD pipeline also wants to (re)deploy when new images are published
        * can be done by 'restarting' and configuring the resources as 'pullLatest'
        * should be capable of rolling updates (blue/green deployments)
        
    * removed K8S configuration and deployment steps from terraform.
    
* Current state of different components/services is not clear, we need to document/show how the different components and layers are related to each other
    * AWS services
    * Kubernetes hosted services/configuration
    * load balancer(s)/ingress/routes/whatever
    * Exposing AWS attributes to the K8S pods for usage (like the Kinesis stream)
    
