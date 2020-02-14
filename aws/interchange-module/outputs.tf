output "cluster_ip" {
  value = module.eks.cluster_endpoint
}

output "kinesis_roads_arn" {
  value = aws_kinesis_stream.interchange_stream.arn
}

output "eks_interchange_host" {
  value = module.eks.cluster_endpoint
}
