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
    
* The configuration is given in `interchange.tf`
* Use `terraform plan` to see what terraform would to when `apply` is done
* To update the AWS infra, run `terraform apply` in the `./aws` folder
* To tear down the AWS infra, run `terraform destroy` in the `./aws` folder