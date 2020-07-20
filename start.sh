#!/usr/bin/env bash
cur_dir=`pwd`

echo "Run database script"
echo "********************************************************"
cd ./database/
sh build.sh
sh start.sh

cd $cur_dir

echo "Run activemq script"
echo "********************************************************"
docker stop activemq
sh ./activemq/start.sh
