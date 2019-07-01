#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: start_rmi_registry [port]"
    exit
fi

if [ ! -f adrenalina-server/target/adrenalina-server-fat.jar ]; then
    echo "adrenalina-server/target/adrenalina-server-fat.jar . You should run mvn package"
    exit
fi

CLASSPATH=adrenalina-server/target/adrenalina-server-fat.jar rmiregistry $1
