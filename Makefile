# Get Graylog version from Maven POM (property: "graylog.version")
GRAYLOG_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="properties"]/*[local-name()="graylog.version"]/text()' pom.xml)

.PHONY: build

build:
	# Build Graylog web interface
	cd ../graylog2-server && mvn generate-resources -pl graylog2-server -B -V
	mvn clean package

.PHONY: init

init:
	# Checkout desired Graylog version
	echo "Checking out Graylog ${GRAYLOG_VERSION}"
	git clone --depth 1 --branch "${GRAYLOG_VERSION}" https://github.com/Graylog2/graylog2-server.git ../graylog2-server

.PHONY: clean

clean:
	mvn clean
