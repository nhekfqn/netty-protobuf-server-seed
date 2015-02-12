#!/bin/sh

cd $(dirname $0)

proto_src=src/main/resources/protobuf
java_out=src/main/java

protoc --proto_path=$proto_src --java_out=$java_out $proto_src/Protocol.proto
