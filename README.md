# Atlas

Atlas is a web application handling inbound events from Internet of Things (IoT) devices.

The events describe the state of the devices and may result to an auto-executed action. 

##### Technology Stack

* Java 9
* Spring Boot 2
* PostgreSQL
* Maven 3

##### Application Configuration

* Default : {PROJECT_HOME}/src/main/resources/atlas.yml 
* Runtime : {SPRING_CONFIG_LOCATION}/atlas.yml (if not found, the app defaults to the one in the classpath)
 
##### Application Database

From a machine with docker installed + internet access, execute:

    docker run -p 5432:5432 --name rdbms -e POSTGRES_PASSWORD=postgres -d postgres:9.4

Make sure your hosts file maps rdbms to localhost

    127.0.0.1       localhost rdbms

Init or migrate the database schema

    Init the database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=atlas -Ddb.module.userId=atlas -Ddb.module.password=atlas -Ddb.root.password=postgres -Dinit.database.skip=false flyway:migrate
    
    Migrate an existing database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=atlas -Ddb.module.userId=atlas -Ddb.module.password=atlas flyway:migrate

##### Application Execution

Atlas is a Spring Boot application thus can be executed either as standalone application or inside an application container (Tomcat 9).
