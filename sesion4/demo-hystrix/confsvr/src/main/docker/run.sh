#!/bin/sh


echo "********************************************************"
echo "Starting Configuration Service with Eureka Endpoint:  $EUREKASERVER_URI";
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI -jar /usr/local/configserver/@project.build.finalName@.jar
