#!/bin/bash

echo "Removing existing 'cardservice/database' image..."
docker rmi --force cardservice/database

echo "Building a new 'cardservice/database' image..."
docker build -t cardservice/database .
