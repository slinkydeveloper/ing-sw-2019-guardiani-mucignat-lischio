#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ -f adrenalina-cli/target/adrenalina-cli-fat.jar ]; then
    jar_location="adrenalina-cli/target/adrenalina-cli-fat.jar"
elif [ -f adrenalina-cli-fat.jar ]; then
    jar_location=adrenalina-cli-fat.jar
else
    echo "Cannot find adrenalina-cli.jar. You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar $jar_location "$@"
