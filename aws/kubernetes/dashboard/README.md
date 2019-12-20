
## Prepare for using the dashboard
* `kubectl apply -f dashboard.yaml`
* `kubectl apply -f eks-admin-service-account.yaml`

## How to get token to access dashboard
```bash
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep eks-admin | awk '{print $1}')
```

## Access dashboard
* Launch the proxy:
    `kubectl proxy`
* Browse to the dashboard
    `http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=default`
