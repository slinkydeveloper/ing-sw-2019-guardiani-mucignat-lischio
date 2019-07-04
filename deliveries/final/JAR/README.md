# Software Engineering 2019 Final exam Guardiani, Lischio

## Build

To execute the build, run on the root directory of the project

```bash
mvn clean package
```

## Execute

The three executable packages are:

* `adrenalina-server-fat.jar`
* `adrenalina-cli-fat.jar`
* `adrenalina-gui-fat.jar`

To execute the server, you need an rmi registry up and running with configured classpath.
To start it, you can use the wrapper script `start_rmi_registry.sh`.

For all executables, you need to configure the system variable `java.rmi.server.hostname` to you LAN IP.
There are three wrapper scripts `start_server.sh`, `start_cli.sh`, `start_gui.sh` that does it automatically. 
To run the scripts you need Linux or Mac OS, for Windows you must configure these variables manually

## Available features

* Complete game rules
* CLI
* GUI
* Socket
* RMI
* Multi-match

## Sonar lint

Sonar must be up and running on port 9000

```bash
mvn clean test sonar:sonar
```

## Javadoc

```bash
mvn javadoc:javadoc
```
    
## PlantUml

Plantuml can be downloaded and builded using script `plantuml_download.sh`

You can create a new deliverable png using `plantuml_run.sh`. Requires ImageMagick's `convert`

Project uml are generated with script `generate_uml.kts` (requires `kscript`, downloadable with `https://sdkman.io/install`)
