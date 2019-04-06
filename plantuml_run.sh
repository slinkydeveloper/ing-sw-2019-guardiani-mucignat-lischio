#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: plantuml_run [labN]"
    exit
fi

if [ ! -f plantuml.jar ]; then
    echo "plantuml.jar not found. Maybe you need to run plantuml_download script first"
    exit
fi

command -v java >/dev/null 2>&1 || { echo >&2 "I require java but it's not installed. Aborting."; exit 1; }
command -v convert >/dev/null 2>&1 || { echo >&2 "I require convert (from ImageMagick) but it's not installed. Aborting."; exit 1; }

java -jar plantuml.jar -tsvg uml/uml.puml
mkdir -p deliveries/$1
convert uml/uml.svg deliveries/$1/uml/uml.png
mv uml/uml.svg deliveries/$1/uml/uml.svg
git add deliveries/$1/uml/uml.png
git add deliveries/$1/uml/uml.svg

echo "Completed!"
