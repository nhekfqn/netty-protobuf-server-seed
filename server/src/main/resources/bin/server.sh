#!/bin/sh

cd $(dirname $0)/..

java -cp "lib/*" com.nhekfqn.sample.server.Main conf/server.properties
