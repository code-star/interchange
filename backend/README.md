# Codestar Interchange backend

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

## Docker

Based on https://ktor.io/quickstart/quickstart/docker.html#building-and-running-the-docker-image and https://reladev.org/ktor-and-docker/

```
docker build -t codestar-interchange .
docker run -m512M --cpus 2 -it -p 8089:8089 --rm codestar-interchange
```
