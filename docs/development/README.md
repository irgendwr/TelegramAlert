# Development

## Getting started

This project is using [Maven](https://maven.apache.org) 3 and requires whatever Java version that Graylog requires (Java 17 or higher?).

```bash
git clone https://github.com/irgendwr/TelegramAlert.git
cd TelegramAlert
# Get Graylog version from Maven POM (property: "graylog.version")
# NOTE: this requires "libxml2-utils"
export GRAYLOG_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="version"]/text()' pom.xml)
# Checkout desired Graylog version
echo "Checking out Graylog ${GRAYLOG_VERSION}"
git clone --depth 1 --branch "${GRAYLOG_VERSION}" https://github.com/Graylog2/graylog2-server.git ../graylog2-server
pushd ../graylog2-server
./mvnw compile
popd
```

## Build

Run `../graylog2-server/mvnw -B package --file pom.xml` to build a JAR file.

Note: You may need to define the correct Java version for Maven, eg. via

```bash
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk"
export PATH="/usr/lib/jvm/java-17-openjdk/bin/:$PATH"
```

*Alternatively, the [Graylog documentation](https://go2docs.graylog.org/5-0/what_more_can_graylog_do_for_me/plugins.html?tocpath=What%20More%20Can%20Graylog%20Do%20for%20Me%253F%7CPlugins%7C_____0#WritingPlugins) describes how to create a convenient setup with hot reloading.*

## Plugin Release

We are using the maven release plugin:

```bash
../graylog2-server/mvnw release:prepare -Dresume=false
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
pushd ../graylog2-server
./mvnw compile
popd
```

or update your graylog-project setup:

```bash
# adjust the path below
cd graylog-project
# replace <VERSION> with the Graylog version (e.g. 6.0.1)
graylog-project graylog-version --force-https-repos --set <VERSION>
```

Re-add `<module>../graylog-project-repos/graylog-plugin-telegram-notification</module>` in `graylog-project/pom.xml`

## Upgrade yarn dependencies

`yarn upgrade`

Afterwards it might be a good idea to check `yarn audit`. It's crazy how many vulnerabilities are around.

## Run mongo and elasticsearch from docker

Run:
```bash
docker run --rm --name mongo -p 27017:27017 -d mongo:5
docker run --rm --name elasticsearch \
    -e "http.host=0.0.0.0" \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -p 9200:9200 -p 9300:9300 \
    -d docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
```

Stop:
```bash
docker stop mongo elasticsearch
docker rm mongo elasticsearch
```

## Troubleshooting

### Unrecognized option: --add-exports

Ensure that Maven uses the correct Java version by setting the `JAVA_HOME` and `PATH` environment variables as shown above.

### class file has wrong version

Ensure that Maven uses the correct Java version by setting the `JAVA_HOME` and `PATH` environment variables as shown above.

### Cannot find module '.../graylog2-server/graylog2-web-interface/manifests/vendor-manifest.json'

```bash
pushd ../graylog2-server
mvn compile
pushd graylog2-web-interface
# Build Vendor Manifest:
yarn install
yarn run build
popd; popd
```

### Package "graylog-web-plugin" refers to a non-existing file

`ln -s ../../graylog2-server target/graylog2-server`
