# Software Engineering 2019 Final exam Guardiani, Mucignat, Lischio


## Maven

* `mvn clean test sonar:sonar`
    * used to analyze Maven project with Sonar
    * requires Sonar already turned on 
    * to be executed in the directory which contains the pom.xml file
    
    Note that you must run test before running sonar, or it won't be able to calculate the coverage
    
* `mvn clean`
    * remove all the files generated by the previous build
    
* `mvn compile`
    * compile the source code of the project
    
* `mvn javadoc:javadoc`
    * generate javadoc
    
## PlantUml

Plantuml can be downloaded and builded using script `plantuml_download.sh`

You can create a new deliverable png using `plantuml_run.sh`
