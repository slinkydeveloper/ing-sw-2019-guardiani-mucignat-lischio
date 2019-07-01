#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ ! -f adrenalina-server/target/adrenalina-server-fat.jar ]; then
    echo "adrenalina-server/target/adrenalina-server-fat.jar . You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar adrenalina-server/target/adrenalina-server-fat.jar
