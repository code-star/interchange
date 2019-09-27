# Using AWS

## Initial steps
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

## Setup for each developer
* Create account for the new user (Admin needs to do this)
    * provide username and email address (mail/verbal to admin)
    * admin to add user, set initial password and optionally send access key info to new user
    * by default developers will be put in CodestarDeveloper group
* IAM login link: https://182176061631.signin.aws.amazon.com/console
    * Users are advised to activate Multifactor Authentication
    * Userd can create their own access key via 'My Security credentials'
* Install AWS CLI - TBD (see internet)
* Configure CLI `aws configure`
    * Enter Access Key `***`
    * Enter Secret Access Key `******`
    * Enter Region `eu-central-1`
* Verify configuration with `aws ec2 describe-instances`

