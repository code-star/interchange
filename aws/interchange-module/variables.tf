
locals {
  profile_name = "default"
  cluster_name = "interchange-cluster"
  region_name = "eu-central-1"
}

variable "map_users" {
  description = "Additional IAM users to add to the aws-auth configmap."
  type = list(object({
    userarn  = string
    username = string
    groups   = list(string)
  }))

  default = [
    {
      userarn  = "arn:aws:iam::182176061631:user/donovan"
      username = "donovan"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/hamza"
      username = "hamza"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/hernan"
      username = "hernan"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/jeanmarc"
      username = "jeanmarc"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/rene"
      username = "rene"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/steven"
      username = "steven"
      groups   = ["system:masters"]
    },
    {
      userarn  = "arn:aws:iam::182176061631:user/werner"
      username = "werner"
      groups   = ["system:masters"]
    },
  ]
}