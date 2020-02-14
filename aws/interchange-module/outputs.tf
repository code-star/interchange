output "cluster_ip" {
  value = module.eks.cluster_endpoint
}

output "kinesis_roads_arn" {
  value = aws_kinesis_stream.interchange_stream.arn
}

output "eks_interchange_host" {
  value = module.eks.cluster_endpoint
}

output "eks_frontend_address" {
  value = kubernetes_service.interchange_frontend_service.load_balancer_ingress.0.hostname
}

output "eks_backend_address" {
  value = kubernetes_service.interchange_backend_service.load_balancer_ingress.0.hostname
}
