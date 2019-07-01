#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: start_cli [network_interface]"
    exit
fi

ip_discovered=$(ifconfig wlp3s0 | awk '/inet /{print $2;}')

echo "Discovered ip $ip_discovered"

if [ ! -f adrenalina-cli/target/adrenalina-cli-fat.jar ]; then
    echo "adrenalina-cli/target/adrenalina-cli-fat.jar . You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$ip_discovered -jar adrenalina-cli/target/adrenalina-cli-fat.jar
