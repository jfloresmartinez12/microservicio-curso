#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $EUREKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"



echo "********************************************************"
echo "Starting License Server with Configuration Service via Eureka :  $EUREKASERVER_URI:$SERVER_PORT"
echo "Using Kafka Server: $KAFKASERVER_URI"
echo "Using ZK    Server: $ZKSERVER_URI"
echo "USing Profile: $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.profiles.active=$PROFILE -jar /usr/local/licensingservice/@project.build.finalName@.jar
