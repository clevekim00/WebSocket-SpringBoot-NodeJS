// NODE_ENV에 지정한 값을 기준으로 파일을 읽어온다.
// default.json / release.json
process.env["NODE_ENV"] = "release";
// NODE_CONFIG_DIR은 config 파일의 위치
// default는 node가 실행되는 디렉토리 기준으로 config/ 이다.
process.env["NODE_CONFIG_DIR"] = "./config";

const W3CWebSocket = require("websocket").w3cwebsocket;
var client;
var SampleProto = require("./proto/sample_pb");

const config = require("config");
const urlWs = config.get("URL");
const protocol = config.get("Protocol");
// ---------------------------------------------------
// WebSocket controller
// ---------------------------------------------------
function openConnectWithW3C() {

    console.log("protocol", protocol);

    client = new W3CWebSocket(urlWs, protocol);
    client.onclose = onClose;
    client.onopen = onOpen;
    client.onmessage = onMessage;
    client.onerror = onError;
}

function onError(error) {
    console.log("Connection is error now", error);
}

function onOpen(opened) {
    console.log("connection is opened now", opened.type);
    testLoop();
}

function onMessage(message) {
    console.log("recv message.type", message.type);
    console.log("recv message.data", message.data);

    var deserialized = protobufDeserialize(message.data);

    console.log("deserialized regist:", deserialized);
    console.log("------------------------------------------------------------");

    testLoop();
}

function onClose(closed) {
    console.log("Connection is closed now.", closed.type);
}

// ---------------------------------------------------
// Test
// ---------------------------------------------------
var testCount = 10;
var tested = 0;
function protobufTest() {
    openConnectWithW3C();
}

function testLoop() {
    if (tested < testCount) {
        client.send(protobufSerialize());
        tested++;
    } else {
        client.close();
    }
}

// ---------------------------------------------------
// Protobuf control
// ---------------------------------------------------
//https://www.npmjs.com/package/google-protobuf
function protobufSerialize() {

    var sample = new proto.sample.Sample();
    sample.setName("sample-" + tested);

    console.log("send sample", sample);
    var serialized = sample.serializeBinary();
    console.log("serialized message", serialized);
    return serialized;
}

function protobufDeserialize(array) {

    return proto.sample.Sample.deserializeBinary(array);
}
// connect();
protobufTest();
