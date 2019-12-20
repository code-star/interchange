# Using AWS

## Getting access

### Initial steps
* Create AWS Account - Done: CodestarNL
* Create Access Key (for root account) - Done
* Setup MFA for root account - Done
* Setup IAM (groups, users, permissions etc)
    * Groups created: 
        * CodestarDeveloper - `SystemAdministrator`, `PowerUserAccess`
        * CodestarAdmin - `AdministratorAccess`
    * Users created:
        * jeanmarc (admin)
* Remove Access Key for root account - Done

### Setup for each developer
* Create account for the new user (Admin needs to do this)
    * provide username and email address (mail/verbal to admin)
    * admin to add user, set initial password and optionally send access key info to new user
    * by default developers will be put in CodestarDeveloper group
* IAM login link:
    * https://codestarnl.signin.aws.amazon.com/console
    * https://182176061631.signin.aws.amazon.com/console
    * Users are advised to activate Multifactor Authentication
    * Userd can create their own access key via 'My Security credentials'
* Install AWS CLI - TBD (see internet)
* Configure CLI `aws configure`
    * Enter Access Key `***`
    * Enter Secret Access Key `******`
    * Enter Region `eu-central-1`
* Verify configuration with `aws ec2 describe-instances`

## Monitoring

* Activate CloudTrail logs to keep track of what is happening in the organisation
    * unless you only need the latest 90 days.
    
## AWS Kubernetes cluster

* Resources: AWS documentation, and https://kubernetes-training.readthedocs.io/en/latest/index.html
* EKS -> Create cluster
    * Create Role to allow EKS to create/configure resources -> https://console.aws.amazon.com/iam/home?#/roles
        * created role `EKS_Manager`
    * Using the `eksctl` cli
        * install `eksctl` (see https://docs.aws.amazon.com/eks/latest/userguide/eksctl.html#installing-eksctl)
        * https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html
        * See `eksctl create cluster` command given below
            ```
            eksctl create cluster \
            --name interchange-cluster \
            --version 1.14 \
            --nodegroup-name interchange-workers \
            --node-type t2.micro \
            --nodes 2 \
            --nodes-min 2 \
            --nodes-max 3 \
            --node-ami auto
            ```
    * Configure Kubernetes CLI
        * aws eks --region eu-west-1 update-kubeconfig --name interchange-cluster
    * Create the deployment/service/ingress
        * See /aws/tryout for examples
    
### Troubleshooting

`kubectl describe pods [podname]`