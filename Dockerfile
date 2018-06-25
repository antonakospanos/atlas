FROM tomcat:9.0.7-jre10

LABEL maintainer="antonakospanos.gmail.com"

# Provision Tomcat with atlas webapp, keystore and server.xml
COPY target/atlas*.war $CATALINA_HOME/webapps/atlas.war
COPY src/main/resources/ssl/atlas.keystore $CATALINA_HOME/conf/localhost-rsa.jks
COPY src/conf/tomcat/server.xml $CATALINA_HOME/conf/server.xml
COPY src/conf/tomcat/tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml

# Expose Ports
EXPOSE 8080
EXPOSE 80
EXPOSE 443

# Start the server
CMD ["catalina.sh", "run"]
