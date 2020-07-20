#!/usr/bin/env bash
cur_dir=`pwd`

echo "Stop database script"
echo "********************************************************"
cd ./database/
sh stop.sh

cd $cur_dir

echo "Stop activemq script"
echo "********************************************************"
sh ./activemq/stop.sh