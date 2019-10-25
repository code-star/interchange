# Codestar Interchange backend

## Docker

Based on https://ktor.io/quickstart/quickstart/docker.html#building-and-running-the-docker-image and https://reladev.org/ktor-and-docker/

```
docker build -t codestar-interchange .
docker run -m512M --cpus 2 -it -p 8089:8089 --rm codestar-interchange
```
