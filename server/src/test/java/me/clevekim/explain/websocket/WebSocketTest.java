package me.clevekim.explain.websocket;

import lombok.extern.slf4j.Slf4j;
import me.clevekim.explain.websocket.protobuf.WebSocketSampleProto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by younghwan.kim@linecorp.com on 2019-05-13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebSocketTest {

    final WebSocketSampleProto.Sample sample = WebSocketSampleProto.Sample.newBuilder().setName("TestName").build();

    WebSocketSession webSocketSession;
    String url = "ws://localhost:9090/ws";
    @Before
    public void setUp() {

        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        try {
            webSocketSession = webSocketClient.doHandshake(getHandler(), url).get();

        } catch (Exception e) {
            log.info("WebSocket client failed", e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void websocketClient_01_OpenConnectionTest() {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        try {
            webSocketClient.doHandshake(getHandler(), url).get();

        } catch (Exception e) {
            log.info("WebSocket client failed", e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void websocketClient_02_SendBinaryMessageTest() {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            sample.writeTo(byteArrayOutputStream);
            BinaryMessage binaryMessage = new BinaryMessage(byteArrayOutputStream.toByteArray());
            webSocketSession.sendMessage(binaryMessage);
        } catch (Exception e) {
            log.error("Send message failed", e);
            Assert.fail(e.getMessage());
        }
    }

    private AbstractWebSocketHandler getHandler() {
        return new AbstractWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {

                log.debug("session:" + session);
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

                Assert.assertTrue(message instanceof BinaryMessage);
                ByteBuffer byteBuffer = ((BinaryMessage) message).getPayload();

                WebSocketSampleProto.Sample recvedSample = WebSocketSampleProto.Sample.parseFrom(byteBuffer.array());

                log.info("recv:" + recvedSample.getName());
                log.info("send:" + sample.getName());
                Assert.assertTrue(sample.getName().equals(recvedSample.getName()));
            }
        };
    }
}
