
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
* `terraform apply` then succeeds (took between 12 and 20 minutes)

### Testing creation and destruction
* created a dummy.tf to create/destroy multiple times
* first run without using kubectl to deploy anything on the k8s cluster
    * creation took 12 minutes
    * destruction took 15 minutes
    * all AWS artifacts were cleaned up
* second run with using kubectl to deploy stuff
    * creation took about 12 minutes
    * destruction timed out after 20 minutes, again leaving the load balancers in place
    * manually cleaned up the load balancers
    * reran the terraform destroy
    * errors out:
        * Error: Error deleting VPC: DependencyViolation: The vpc 'vpc-0be94d9b97183a6af' has dependencies and cannot be deleted.
    * remaining security groups for k8s were not cleaned up
    * manually deleted vpc
* manually deleted Vincent's Kinesis streams in Ohio