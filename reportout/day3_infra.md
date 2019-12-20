
# Day 3 Infra

## Cleanup from day 2
Left over resources, manually deleted:

* VPC - 3 entries
    * 2 could be deleted immediately
    * 1 contained 2 active network interfaces that needed to be deleted first
        * I did not have permissions to detach the network interface (needed to detach before delete)
          because they are AWS managed - need to delete the related service. Turns out this were load
          balancers
        * after deleting the load balancers, the VPC could be deleted as well
* Load balancers - 2 entries
    * removing them also cleaned up the 2 active network interfaces
* Elastic IP addresses - not attached to running instances - 2 entries


## Day3 creation of infra
* Ran the following commands in the ./aws folder:
    ```shell script
    terraform init
    terraform refresh
    terraform apply
    
    ```
    Then wait for 10 minutes.
* Errors out because terraform thinks it should use a desired number of 1 instance instead of 3.
* `terraform destroy` to clean up the broken setup
* remove `.terraform` folder and  and `terraform.tfstate` file (and its backup)
* run `terraform init` anf `terraform refresh`
* try `terraform plan` and `terraform apply` again
* Failed with same error
* change `desired_capacity` to `asg_desired_capacity`
* `terraform apply`