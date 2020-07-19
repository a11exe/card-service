#!/usr/bin/env bash

echo "Pull activemq container from rmohr/activemq"
echo "********************************************************"
docker pull rmohr/activemq

echo "Waiting for the activemq docker container to stop"
echo "********************************************************"
docker stop activemq

echo "Waiting for the activemq docker container to remove"
echo "********************************************************"
docker container rm activemq

echo "Start activemq docker container"
echo "********************************************************"
docker run -p 61616:61616 -p 8161:8161 -d --name activemq rmohr/activemq
