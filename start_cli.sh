#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ ! -f adrenalina-cli/target/adrenalina-cli-fat.jar ]; then
    echo "adrenalina-cli/target/adrenalina-cli-fat.jar . You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar adrenalina-cli/target/adrenalina-cli-fat.jar "$@"
