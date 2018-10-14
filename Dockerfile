FROM maven:3.5-jdk-11 as build-stage

COPY . /usr/src/myapp
RUN mvn -f /usr/src/myapp/pom.xml clean package

FROM tomcat:9-jre11
RUN apt-get update && apt-get install -y vim less
LABEL maintainer="antonakospanos.gmail.com"

# Provision Tomcat with keystore and server.xml
COPY src/main/resources/ssl/atlas.keystore $CATALINA_HOME/conf/atlas.jks
COPY src/conf/tomcat/server.xml $CATALINA_HOME/conf/server.xml
COPY src/conf/tomcat/tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml
COPY src/conf/tomcat/setenv.sh $CATALINA_HOME/bin/setenv.sh

# Provision Tomcat with webapp and default configuration
RUN mkdir $CATALINA_HOME/conf-apps
ADD src/main/resources/atlas-* $CATALINA_HOME/conf-apps/
COPY --from=build-stage /usr/src/myapp/target/atlas*.war $CATALINA_HOME/webapps/atlas.war

# Expose Ports
EXPOSE 80
EXPOSE 443
EXPOSE 8080
EXPOSE 8443

# Start the server
CMD ["catalina.sh", "run", "-Dspring.config.location=$CATALINA_HOME/conf-apps/"]