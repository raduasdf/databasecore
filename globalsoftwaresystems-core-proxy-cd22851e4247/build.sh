#!/bin/bash

version=
app="core-proxy"
maven_version="3-jdk-8"
registry="core-repo"
if [ ! -z $1 ]; then 
	version=$1
else
	exit 1
fi

docker run -v /etc/group:/etc/group:ro \
	-v /etc/passwd:/etc/passwd:ro --rm \
	-v /home/$USER:/home/$USER \
	-v "$PWD":/workdir \
	-w /workdir \
	-u $(id -u $USER):$(id -g $USER) node:9.2.0 \
	npm install --prefix ./frontend/project
	
docker run -v /etc/group:/etc/group:ro \
	-v /etc/passwd:/etc/passwd:ro --rm \
	-v /home/$USER:/home/$USER \
	-v "$PWD":/workdir \
	-w /workdir \
	-u $(id -u $USER):$(id -g $USER) node:9.2.0 \
	npm run build-prod --prod --prefix ./frontend/project

docker run --rm -v ~/:/var/maven \
    -v "$(pwd)":/usr/src/mymaven \
    -u "$(id -u)":"$(id -g)" \
    -w /usr/src/mymaven -e MAVEN_CONFIG=/var/maven/.m2 \
	maven:${maven_version} \
	mvn -D user.home=/var/maven clean package -DskipTests || exit $?

docker build -t ${registry}/${app}:${version} . || exit $?
docker tag ${registry}/${app}:${version} ${registry}/${app}:latest
## docker push ${registry}/${app}:${version} || exit $?
## docker push ${registry}/${app}:latest || exit $?

exit $?