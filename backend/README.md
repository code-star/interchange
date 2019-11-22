# Codestar Interchange backend

## Getting started with AWS

Ask one of your colleagues to get you an account on the Codestar environment (login through https://codestarnl.signin.aws.amazon.com/console). 

Open up "My security credentials" and create an access key. Be sure to write down the Access Key ID and the Secret Access Key.

Configure the AWS CLI (https://docs.aws.amazon.com/cli/latest/userguide/install-macos.html), with `aws configure`. It will ask you for the keys you just wrote down.

To test it all works, 

```bash
aws kinesis list-streams
```

You should see the stream "Roads" listed there.

When you run the application, the Kinesis connection library will pick up these settings from your configuration file (`~/.aws/credentials`) and connect using that.

## Configuring Kinesis to run locally

To run the application locally, we need to be able to connect to a Kinesis server (which also runs locally). To do this, we need to install LocalStack (https://github.com/localstack/localstack):

```bash
pip install localstack
```

or

```bash
pip3 install localstack
```

and run it (in the background somewhere):

```bash
SERVICES="kinesis:4567" localstack start --host
```

Alternatively, we can use the (much lighter) Kinesalite: https://github.com/mhart/kinesalite

Whatever you try, you need to create the streams before running the application:

```bash
aws --endpoint-url=http://localhost:4567 kinesis create-stream --stream-name string --shard-count 1
```

Verify that it worked with:

```bash
aws --endpoint-url=http://localhost:4567 kinesis list-streams
```

## Running the application
### From IntelliJ
* Make sure you have the Gradle plugin installed.
* Open the project in IntelliJ.
* Create a new Run Configuration using “Application” as a template
    * Use `nl.codestar.interchange.ApplicationKt` as the entry point
    * Use `interchange.main` as module
    * Use the Java X as the JRE
* Save the Configuration by giving it a name
* Right-click on the module (`back-end`) and choose "Open module settings"
* Under "Project", choose the same Java version as you specified in the run configuration.
* Run the project; check that you have a response under `http://0.0.0.0:8080/api/v0/route`

## Build a docker image

Based on https://ktor.io/quickstart/quickstart/docker.html#building-and-running-the-docker-image and https://reladev.org/ktor-and-docker/

```
docker build -t codestar-interchange .
# To run the docker image locally
docker run -m512M --cpus 2 -it -p 8089:8089 --rm codestar-interchange
```

## Pushing to K8s

* Log in to the ECR docker registry `$(AWS_PROFILE="codestar" AWS_DEFAULT_REGION="eu-central-1" aws ecr get-login --no-include-email --region eu-central-1)`
* Tag the image `docker tag codestar-interchange:latest 182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange:latest`
* Push it to ECR: `docker push 182176061631.dkr.ecr.eu-central-1.amazonaws.com/codestar-interchange:latest`

