#!/usr/bin/env bash

source load_ip.sh

get_ip
echo "Discovered ip $discovered_ip"

if [ -f adrenalina-gui/target/adrenalina-gui-fat.jar ]; then
    jar_location="adrenalina-gui/target/adrenalina-gui-fat.jar"
elif [ -f adrenalina-gui-fat.jar ]; then
    jar_location=adrenalina-gui-fat.jar
else
    echo "Cannot find adrenalina-gui.jar. You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$discovered_ip -jar $jar_location "$@"
