#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: start_server [network_interface]"
    exit
fi

ip_discovered=$(ifconfig wlp3s0 | awk '/inet /{print $2;}')

echo "Discovered ip $ip_discovered"

if [ ! -f adrenalina-server/target/adrenalina-server-fat.jar ]; then
    echo "adrenalina-server/target/adrenalina-server-fat.jar . You should run mvn package"
    exit
fi

java -Djava.rmi.server.hostname=$ip_discovered -jar adrenalina-server/target/adrenalina-server-fat.jar
