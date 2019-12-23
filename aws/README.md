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
    * Users can create their own access key via 'My Security credentials'
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

* Resources: 
    * AWS documentation, and https://kubernetes-training.readthedocs.io/en/latest/index.html
    * Terraform documentation: https://www.terraform.io/

* EKS
    * Using Terraform to create cluster - see `interchange.tf` for details
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
        * `aws eks --profile codestar --region eu-central-1 update-kubeconfig --name interchange-cluster`
        * replace the profile with an appropriate profile if you don't want to use the `codestar` name
    * Create the deployment/service/ingress
        * See `./aws/tryout` for examples, use `kubectl apply -f <filename>`
        
### Accessing the kubernetes dashboard
Based on https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html

* Ensure your AWS userID is present in the auth_config(?) config map
* Ensure Kubernetes CLI is configured to work with the EKS cluster (see above)
* See `aws/kubernetes/dashboard/README.md`
    * Prepare the dashboard by applying the yamls in `./aws/kubernetes/dashboard/`
    * Get an access token for the dashboard
        `kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep eks-admin | awk '{print $1}')`
    * Copy the access token
    * Launch the proxy:
        `kubectl proxy`
    * Browse to the dashboard
        `http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=default`
    
### Troubleshooting

`kubectl describe pods [podname]`

### Setup service account
* `kubectl apply -f kubernetes/initial/service-account.yml`
* `kubectl get serviceaccounts/build-robot -o yaml`
    * this shows the name of the generated secret (`...name: build-robot-token-XXX`)
* lookup the token via
    * `kubectl get secret build-robot-token-XXX -o yaml`
* store the token as a secret for the github actions
    * go to `https://github.com/code-star/interchange/settings/secrets`
    * remove `K8S_SECRET` (old secret, if needed)
    * create new `K8S_SECRET` secret, paste the complete yaml output of the previous step into the secret

### Create backend services and ingress
* `kubectl apply -f kubernetes/backend.yaml`
* `kubectl apply -f kubernetes/backend-service.yml`
* `kubectl apply -f kubernetes/backend-ingress.yml`
