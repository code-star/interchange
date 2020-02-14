provider "aws" {
  profile    = "default"
  region     = "eu-central-1"
}

locals {
  cluster_name = "dummy-cluster"
}

output "cluster_ip" {
  value = module.eks.cluster_endpoint
}

module "eks" {
  config_output_path = "./target/"
  source       = "terraform-aws-modules/eks/aws"
  cluster_name = local.cluster_name
  subnets      = module.vpc.private_subnets
  vpc_id       = module.vpc.vpc_id

  worker_groups = [
    {
      instance_type = "t2.micro"
      asg_min_size = 3
      asg_max_size  = 3
      asg_desired_capacity = 3
    }
  ]

  map_users                            = var.map_users

  tags = {
    environment = "test"
  }
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "2.6.0"

  name                 = "dummy-vpc"
  cidr                 = "10.1.0.0/16"
  azs                  = ["eu-central-1a", "eu-central-1b"]
  private_subnets      = ["10.1.1.0/24", "10.1.2.0/24"]
  public_subnets       = ["10.1.4.0/24", "10.1.5.0/24"]
  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true

  tags = {
    "kubernetes.io/cluster/${local.cluster_name}" = "shared"
  }

  public_subnet_tags = {
    "kubernetes.io/cluster/${local.cluster_name}" = "shared"
    "kubernetes.io/role/elb"                      = "1"
  }

  private_subnet_tags = {
    "kubernetes.io/cluster/${local.cluster_name}" = "shared"
    "kubernetes.io/role/internal-elb"             = "1"
  }
}

resource "aws_kinesis_stream" "dummy_stream" {
  enforce_consumer_deletion = true

  name             = "DummyRoads"
  shard_count      = 1
  retention_period = 24

  tags = {
    Environment = "test"
  }
}