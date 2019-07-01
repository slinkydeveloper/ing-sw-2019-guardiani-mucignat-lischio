#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ ! -f adrenalina-gui/target/adrenalina-gui-fat.jar ]; then
    echo "adrenalina-gui/target/adrenalina-gui-fat.jar . You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar adrenalina-gui/target/adrenalina-gui-fat.jar
