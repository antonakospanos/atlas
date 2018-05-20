# Atlas

Atlas is a web application handling inbound events from Internet of Things (IoT) devices.

The events describe the state of the devices and may result to an auto-executed action. 

##### Technology Stack

* Java 10
* Spring Boot 2
* PostgreSQL 9.4
* MQTT Broker
* Maven 3

##### Database

From a machine with docker installed + internet access, execute:

    docker run -p 5432:5432 --name rdbms -e POSTGRES_PASSWORD=postgres -d postgres:9.4

Make sure your hosts file maps rdbms to localhost

    127.0.0.1       localhost rdbms

Init or migrate the database schema

    Init the database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=atlas -Ddb.module.userId=atlas -Ddb.module.password=atlas -Ddb.root.password=postgres -Dinit.database.skip=false flyway:migrate
    
    Migrate an existing database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=atlas -Ddb.module.userId=atlas -Ddb.module.password=atlas flyway:migrate

##### MQTT Broker

From a machine with docker installed + internet access, download image 'peez/hivemq' or create a new one using DockerFile-HiveMQ:
```
docker run -p 1883:1883 --name hivemq -d peez/hivemq
```
```
docker build -t hivemq-broker -f Dockerfile-HiveMQ .
docker run -p 1883:1883 --name hivemq -d hivemq-broker
```

Make sure your hosts file maps rdbms to localhost

    127.0.0.1       localhost broker

##### Application Configuration

* Default : {PROJECT_HOME}/src/main/resources/atlas-application.yml 
* Runtime : {SPRING_CONFIG_LOCATION}/atlas-application.yml (if not found, the app defaults to the one in the classpath)

##### Application Execution

Atlas is a Spring Boot application thus can be executed as a standalone application, inside a servlet container (Tomcat 9) or running a docker container:
```
java -jar atlas.jar   --spring.config.location=atlas-application.yml
```
```
$CATALINA_HOME/bin/startup.sh   --Dspring.config.location=atlas-application.yml
```
```
docker build -t atlas .
docker run -p 8080:8080 -p 443:443 -p 80:80 -d --name atlas -d atlas   -e SPRING_CONFIG_LOCATION=atlas-application.yml
```
