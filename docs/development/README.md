# Development

## Getting started

This project is using [Maven](https://maven.apache.org) 3 and requires Java 8 or higher.

```bash
git clone https://github.com/irgendwr/TelegramAlert.git
cd TelegramAlert
# Get Graylog version from Maven POM (property: "graylog.version")
# NOTE: this requires "libxml2-utils"
export GRAYLOG_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="version"]/text()' pom.xml)
# Checkout desired Graylog version
echo "Checking out Graylog ${GRAYLOG_VERSION}"
git clone --depth 1 --branch "${GRAYLOG_VERSION}" https://github.com/Graylog2/graylog2-server.git ../graylog2-server
# Build Graylog web interface
pushd ../graylog2-server
mvn generate-resources -pl graylog2-server -B -V
popd
```

## Build

Run `mvn clean package` to build a JAR file.

Note: You may need to define the correct Java version for Maven, eg. via `export JAVA_HOME=/usr/lib/jvm/java-8-openjdk`

*Alternatively, the [Graylog documentation](https://docs.graylog.org/en/latest/pages/plugins.html) describes how to create a convenient setup with hot reloading.*

## Plugin Release

We are using the maven release plugin:

```bash
mvn release:prepare -Dresume=false
```

This sets the version numbers, creates a tag and pushes to GitHub. GitHub will build and release artifacts automatically.

## Update Graylog Project Parent

Set the `version` of `parent` in `pom.xml`.

Then, update (clone or checkout) your local graylog2-server as shown in "Getting started":

```bash
# Delete old Graylog-Server
rm -rf ../graylog2-server

export GRAYLOG_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="version"]/text()' pom.xml)
# Checkout desired Graylog version
echo "Checking out Graylog ${GRAYLOG_VERSION}"
git clone --depth 1 --branch "${GRAYLOG_VERSION}" https://github.com/Graylog2/graylog2-server.git ../graylog2-server
# Build Graylog web interface
pushd ../graylog2-server
mvn generate-resources -pl graylog2-server -B -V
popd
```

or update your graylog-project setup:

```bash
# adjust the path below
cd graylog-project
# replace <VERSION> with the Graylog version (e.g. 4.2.7)
graylog-project graylog-version --force-https-repos --set <VERSION>
```

Re-add `<module>../graylog-project-repos/graylog-plugin-telegram-notification</module>` in `graylog-project/pom.xml`

## Upgrade yarn dependencies

`yarn upgrade`

Afterwards it might be a good idea to check `yarn audit`. It's crazy how many vulnerabilities are around.

## Run mongo and elasticsearch from docker

Run:
```bash
docker run --rm --name mongo -p 27017:27017 -d mongo:3
docker run --rm --name elasticsearch \
    -e "http.host=0.0.0.0" \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -p 9200:9200 -p 9300:9300 \
    -d docker.elastic.co/elasticsearch/elasticsearch-oss:6.8.5
```

Stop:
```bash
docker stop mongo elasticsearch
docker rm mongo elasticsearch
```

## Troubleshooting

### class file has wrong version

Ensure that Maven uses the correct Java version by setting the `JAVA_HOME` environment variable, eg. via `export JAVA_HOME=/usr/lib/jvm/java-8-openjdk`.

### Package "graylog-web-plugin" refers to a non-existing file

`ln -s ../../graylog2-server target/graylog2-server`
