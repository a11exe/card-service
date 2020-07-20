#!/usr/bin/env bash
docker stop activemq
docker container rm activemq
docker rmi --force rmohr/activemq