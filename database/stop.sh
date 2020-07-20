#!/usr/bin/env bash
docker stop postgres
docker container rm postgres
docker rmi --force cardservice/database
