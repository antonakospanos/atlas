FROM tomcat:9.0.7-jre10

LABEL maintainer="antonakospanos.gmail.com"

# Provision Tomcat with atlas webapp, keystore and server.xml
COPY target/atlas*.war $CATALINA_HOME/webapps/atlas.war
COPY src/main/resources/ssl/atlas.keystore /opt/tomcat/conf/localhost-rsa.jks
COPY src/conf/tomcat/server.xml /opt/tomcat/conf/server.xml

# Add env variables
ENV SPRING_CONFIG_LOCATION /opt/tomcat/conf/

# Expose Ports
EXPOSE 8080
EXPOSE 80
EXPOSE 443