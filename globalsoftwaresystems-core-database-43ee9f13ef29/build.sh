#!/bin/bash

## Author: Petre Rosioru

version=
app="core-database"
maven_version="3-jdk-8"
## To replace with docker registry...
registry="core-repo"
if [ ! -z $1 ]; then 
	version=$1
else
	exit 1
fi

echo "Current build parameters: $app:$version with maven $maven_version ... "
	
docker run --rm -v ~/:/var/maven \
    -v "$(pwd)":/usr/src/mymaven \
    -u "$(id -u)":"$(id -g)" \
    -w /usr/src/mymaven -e MAVEN_CONFIG=/var/maven/.m2 \
	maven:${maven_version} \
	mvn -D user.home=/var/maven clean package -DskipTests || exit $?

docker build -t ${registry}/${app}:${version} . || exit $?
docker tag ${registry}/${app}:${version} gcr.io/penny-int-websites/${app}:latest
## docker push ${repository}/${app}:${version} || exit $?
## docker push ${repository}/${app}:latest || exit $?

exit $?