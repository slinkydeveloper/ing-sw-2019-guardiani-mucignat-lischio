#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ -f adrenalina-server/target/adrenalina-server-fat.jar ]; then
    jar_location="adrenalina-server/target/adrenalina-server-fat.jar"
elif [ -f adrenalina-server-fat.jar ]; then
    jar_location=adrenalina-server-fat.jar
else
    echo "Cannot find adrenalina-server.jar. You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar $jar_location "$@"
