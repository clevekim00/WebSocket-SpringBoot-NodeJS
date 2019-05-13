#!/usr/bin/env bash
export HOME_PATH=/Users/user/Project/Study/WebSocket-SpringBoot-NodeJS
export DA_SERVER_HOME_PATH=${HOME_PATH}/server
export DA_AGENT_HOME_PATH=${HOME_PATH}/client/proto
export OUTPUT_JAVA_PATH=${DA_SERVER_HOME_PATH}/src/main/java
export OUTPUT_JS_PATH=${DA_AGENT_HOME_PATH}
export PROTO_PATH=${DA_SERVER_HOME_PATH}/src/main/resources/protobuf

protoc --java_out=${OUTPUT_JAVA_PATH} -I ${PROTO_PATH} ${PROTO_PATH}/*.proto
protoc --js_out=import_style=commonjs,binary:${OUTPUT_JS_PATH} -I ${PROTO_PATH} ${PROTO_PATH}/*.proto

