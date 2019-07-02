#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Usage: plantuml_run [input_file]"
    exit
fi

if [ ! -f plantuml.jar ]; then
    echo "plantuml.jar not found. Maybe you need to run plantuml_download script first"
    exit
fi

command -v java >/dev/null 2>&1 || { echo >&2 "I require java but it's not installed. Aborting."; exit 1; }
command -v convert >/dev/null 2>&1 || { echo >&2 "I require convert (from ImageMagick) but it's not installed. Aborting."; exit 1; }

filename=$(echo "$1" | cut -f 1 -d '.')

java -jar plantuml.jar -tsvg $1
convert $filename.svg $filename.png

echo "Completed!"
