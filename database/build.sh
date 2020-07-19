#!/bin/bash

echo "Removing existing 'cardservice/database' image..."
echo "********************************************************"
docker rmi --force cardservice/database

echo "Building a new 'cardservice/database' image..."
echo "********************************************************"
docker build -t cardservice/database .
