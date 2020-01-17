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
    * in `./aws/interchange.tf` (pre Day 4 config)
    * in `./aws/interchange-module/variables.tf` (Day 4 onwards)
* in the `./aws/interchange-module` folder
    * remove `.terraform` folder
    * remove any `terraform.tfstate` and `...tfstate.backup` files
    * `terraform init`
        * This will install the aws plugin for terraform
    * `terraform refresh`
        * Make sure all needed files are present and local state is populated with actual resources
    * `terraform plan`
        * Prepare for creating/updating the infrastructure
    * `terraform apply`
        * perform updates
    
* The configuration is given in `interchange-module/main.tf`
* Use `terraform plan` to see what terraform would to when `apply` is done
* To update the AWS infra, run `terraform apply` in the `./aws/interchange-module` folder
* To tear down the AWS infra, run `terraform destroy` in the `./aws/interchange-module` folder