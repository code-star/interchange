## Build a docker image

Based on https://ktor.io/quickstart/quickstart/docker.html#building-and-running-the-docker-image and https://reladev.org/ktor-and-docker/

```
docker build -t codestar-interchange-data .
# To run the docker image locally
docker run -m512M --cpus 2 -it -p 8089:8089 --rm codestar-interchange-data
```

## Pushing to K8s

* Log in to the ECR docker registry `$(AWS_PROFILE="codestar" AWS_DEFAULT_REGION="eu-central-1" aws ecr get-login --no-include-email --region eu-central-1)`
* Tag the image `docker tag codestar-interchange:latest 182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange-data:latest`
* Push it to ECR: `docker push 182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange-data:latest`

