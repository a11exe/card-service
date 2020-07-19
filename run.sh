#!/usr/bin/env bash
cur_dir=`pwd`

echo "Run database script"
echo "********************************************************"
cd ./database/
sh build.sh
sh run.sh

cd $cur_dir

echo "Run activemq script"
echo "********************************************************"
docker stop activemq
sh ./activemq/run.sh
