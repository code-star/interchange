
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

