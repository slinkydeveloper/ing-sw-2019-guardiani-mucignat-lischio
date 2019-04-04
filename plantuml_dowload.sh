#!/usr/bin/env bash

command -v git >/dev/null 2>&1 || { echo >&2 "I require git but it's not installed. Aborting."; exit 1; }
command -v mvn >/dev/null 2>&1 || { echo >&2 "I require mvn but it's not installed. Aborting."; exit 1; }

rm -rf plantuml_repo
git clone git@github.com:plantuml/plantuml.git plantuml_repo
cd plantuml_repo
git checkout tags/v1.2019.4
mvn package -DskipTests -Dmaven.javadoc.skip=true
mv target/plantuml-1.2019.5-SNAPSHOT.jar ../plantuml.jar
rm -rf plantuml_repo

echo "plantuml.jar builded. Use it with java -jar plantuml.jar file1 file2 file3"
