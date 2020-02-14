provider "aws" {
  profile    = local.profile_name
  region     = local.region_name
}

data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
  load_config_file       = false
  version                = "1.10.0"
}

module "eks" {
  config_output_path = "../target/"
  source       = "terraform-aws-modules/eks/aws"
  cluster_name = local.cluster_name
  subnets      = module.vpc.private_subnets
  vpc_id       = module.vpc.vpc_id

  worker_groups = [
    {
      instance_type = "t2.micro"
      asg_min_size = 4
      asg_max_size  = 4
      asg_desired_capacity = 4
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

  name                 = "interchange-vpc"
  cidr                 = "10.0.0.0/16"
  azs                  = ["eu-central-1a", "eu-central-1b"]
  private_subnets      = ["10.0.1.0/24", "10.0.2.0/24"]
  public_subnets       = ["10.0.4.0/24", "10.0.5.0/24"]
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

resource "aws_kinesis_stream" "interchange_stream" {
  enforce_consumer_deletion = true

  name             = "Roads"
  shard_count      = 1
  retention_period = 24

  tags = {
    Environment = "test"
  }
}

resource "kubernetes_ingress" "interchange_ingress" {
  metadata {
    name = "interchange-ingress"
  }

  spec {
    backend {
      service_name = "codestar-interchange-frontend-service"
      service_port = 8080
    }
    rule {
      http {
        path {
          backend {
            service_name = "codestar-interchange-backend-service"
            service_port = 8080
          }
          path = "/api/*"
        }
      }
    }
  }
}

resource "kubernetes_service" "interchange_backend_service" {
  metadata {
    name = "codestar-interchange-backend-service"
  }

  spec {
    selector = {
      app = "codestar-interchange-backend"
    }
    port {
      port = 8080
      target_port = 8081
      protocol = "TCP"
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_service" "interchange_frontend_service" {
  metadata {
    name = "codestar-interchange-frontend-service"
  }

  spec {
    selector = {
      app = "codestar-interchange-frontend"
    }
    port {
      port = 8080
      target_port = 80
      protocol = "TCP"
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_service" "interchange_router_service" {
  metadata {
    name = "codestar-interchange-router-service"
  }

  spec {
    selector = {
      app = "codestar-interchange-router"
    }
    port {
      port = 8080
      target_port = 80
      protocol = "TCP"
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "interchange_frontend_deployment" {
  metadata {
    name = "codestar-interchange-frontend-deployment"
    labels = {
      app = "codestar-interchange-frontend"
    }
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "codestar-interchange-frontend"
      }
    }
    template{
      metadata {
        labels = {
          app = "codestar-interchange-frontend"
        }
      }
      spec{
        container {
          name = "codestar-interchange-frontend"
          image = "182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange-frontend:latest"
          image_pull_policy = "IfNotPresent"
          resources {
            limits {
              cpu = "0.5"
              memory = "512Mi"
            }
            requests {
              cpu = "250m"
              memory = "50Mi"
            }
          }
          port {
            container_port = 80
          }
        }
      }
    }
  }

}

resource "kubernetes_deployment" "interchange_backend_deployment" {
  metadata {
    name = "codestar-interchange-backend-deployment"
    labels = {
      app = "codestar-interchange-backend"
    }
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "codestar-interchange-backend"
      }
    }
    template{
      metadata {
        labels = {
          app = "codestar-interchange-backend"
        }
      }
      spec{
        container {
          name = "codestar-interchange-backend"
          image = "182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange-backend:latest"
          image_pull_policy = "IfNotPresent"
          resources {
            limits {
              cpu = "0.5"
              memory = "512Mi"
            }
            requests {
              cpu = "250m"
              memory = "50Mi"
            }
          }
          port {
            container_port = 8081
          }
        }
      }
    }
  }

}

resource "kubernetes_deployment" "interchange_router_deployment" {
  metadata {
    name = "codestar-interchange-router-deployment"
    labels = {
      app = "codestar-interchange-router"
    }
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "codestar-interchange-router"
      }
    }
    template{
      metadata {
        labels = {
          app = "codestar-interchange-router"
        }
      }
      spec{
        container {
          name = "codestar-interchange-router"
          image = "hub.docker.com/nginx:latest"
          image_pull_policy = "IfNotPresent"
          resources {
            limits {
              cpu = "0.5"
              memory = "512Mi"
            }
            requests {
              cpu = "250m"
              memory = "50Mi"
            }
          }
          port {
            container_port = 80
          }
        }
      }
    }
  }

}

