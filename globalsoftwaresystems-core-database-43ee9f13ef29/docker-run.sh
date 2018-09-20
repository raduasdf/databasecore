#!/bin/bash

JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=$TZ -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"

exec java -Dsun.misc.URLClassPath.disableJarChecking=true $JAVA_OPTS -jar /app.jar