# Terraforming Interchange Infra
## Setup
* install Terraform CLI (https://www.terraform.io/)
    * Download the CLI Zip
    * Unzip into a folder that is on your path (it's a single binary)
* optionally install the Terraform plugin in IntelliJ
    * IntelliJ prompts you to install once you create/open a .tf file
* read the intro: https://www.terraform.io/intro/index.html
* read the getting started: https://learn.hashicorp.com/terraform/getting-started

## Configuration
* set the correct region (`eu-central-1`)
* in the `./aws` folder run
    * `terraform init`
    * This will install the aws plugin for terraform
* Refresh local state with actual resources:
    * `terraform refresh`
    
* The configuration is given in `./aws/interchange.tf`
* To update the AWS infra, run `terraform apply` in the `./aws` folder
* To tear down the AWS infra, run `terraform destroy` in the `.aws` folder

## Accessing the kubernetes dashboard
Based on https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html

* Ensure your AWS userID is present in the auth_config(?) config map
* Create a kubeconfig for the EKS cluster. Replace the profile with the AWS profile if not the default.
    `aws eks --profile codestar --region eu-central-1 update-kubeconfig --name interchange-cluster`
* Get an access token for the dashboard
    `kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep eks-admin | awk '{print $1}')`
* Copy the access token
* Launch the proxy:
    `kubectl proxy`
* Browse to the dashboard
    `http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=default`
