#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: start_rmi_registry [port]"
    exit
fi

if [ -f adrenalina-server/target/adrenalina-server-fat.jar ]; then
    jar_location="adrenalina-server/target/adrenalina-server-fat.jar"
elif [ -f adrenalina-server-fat.jar ]; then
    jar_location="adrenalina-server-fat.jar"
else
    echo "Cannot find adrenalina-server.jar. You should run mvn package"
    exit
fi

CLASSPATH=$jar_location rmiregistry $1
