#!/bin/bash

DOCKER_DB_PORT="5433"
POSTGRESS_PASSWORD="postgres"

function checkForPortAvailability() {
  nc -z localhost $DOCKER_DB_PORT &> /dev/null
  portAvailable=$?
  if [ $portAvailable == 0 ]
    then
      portAvailabilityMessage="is not available"
    else
      portAvailabilityMessage="is available"
  fi
}

function stopLocalhostPostgressService() {
  checkForPortAvailability

  echo "Port " $DOCKER_DB_PORT $portAvailabilityMessage

  if [ $portAvailable == 0 ]
  then
    while true; do
      read -p "Do you want to stop postgress (y/n)?" yn
      case $yn in
        [Yy]* ) echo "Trying to stop postgress"
                sudo kill $(sudo lsof -t -i:$DOCKER_DB_PORT)
                sleep 3
                checkForPortAvailability
                if [ $portAvailable == 0 ]
                then
                  sudo kill -9 $(sudo lsof -t -i:$DOCKER_DB_PORT)
                fi
                break
                ;;
        [Nn]* ) break;;
        * ) echo "Please answer yes(y) or no(n).";;
      esac
    done
    checkForPortAvailability
  fi
}

function checkPostgressConncection() {
  let attempts=10
  until PGPASSWORD=$POSTGRESS_PASSWORD psql -h localhost -p $DOCKER_DB_PORT -U postgres -w -c '\l' &>/dev/null
  do
    echo "Postgres is unavailable - waiting"
    sleep 3
    ((attempts--))

    echo "attempts left " $attempts
    if [ $attempts -le 0 ]
    then
      break;
    fi
  done

  if [ $attempts -le 0 ]
    then
      echo "!!!ERROR Postgres start failed!"
    else
      PGPASSWORD=$POSTGRESS_PASSWORD psql -h localhost -p $DOCKER_DB_PORT -U postgres -w -c '\l'
      echo "Postgres is up successfully!"
  fi
}



echo "********************************************************"
echo "Docker db port is "$DOCKER_DB_PORT

echo "Waiting for the postgres docker container to stop"
docker stop postgres

echo "Waiting for the postgres docker container to remove"
docker container rm postgres

echo "Checking is port $DOCKER_DB_PORT available"

stopLocalhostPostgressService

if [ $portAvailable == 0 ]
then
  echo "!!! Start FAILED. Port ${DOCKER_DB_PORT} ${portAvailabilityMessage}"
else
  echo "Waiting for the postgres container to start on port $DOCKER_DB_PORT"
  docker run -p $DOCKER_DB_PORT:5432 -d --name postgres cardservice/database
  checkPostgressConncection
fi
