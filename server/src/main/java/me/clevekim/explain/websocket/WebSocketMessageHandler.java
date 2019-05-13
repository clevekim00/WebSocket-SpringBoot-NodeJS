package me.clevekim.explain.websocket;

import lombok.extern.slf4j.Slf4j;
import me.clevekim.explain.websocket.protobuf.WebSocketSampleProto;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by clevekim@naver.com on 2019-05-13
 */
@Slf4j
public class WebSocketMessageHandler  implements WebSocketHandler, SubProtocolCapable {

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        log.info("Session Established Id[" + webSocketSession.getId() + "], Protocol[" + webSocketSession.getAcceptedProtocol() + "]");
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        log.info("getId:" + webSocketSession.getId());
        log.info("getAcceptedProtocol:" + webSocketSession.getAcceptedProtocol());
        log.info("isOpen:" + webSocketSession.isOpen());
        log.info("getRemoteAddress:" + webSocketSession.getRemoteAddress());
        log.info("getBinaryMessageSizeLimit:" + webSocketSession.getBinaryMessageSizeLimit());
        log.info("payload:" + webSocketMessage.getPayload());
        log.info("payloadLength:" + webSocketMessage.getPayloadLength());
        log.info("isLast:" + webSocketMessage.isLast());

        ByteBuffer byteBuffer;
        if (webSocketMessage instanceof TextMessage) {
            byteBuffer = ByteBuffer.wrap(((TextMessage) webSocketMessage).asBytes());
            // text msg
        } else {
            byteBuffer = ((BinaryMessage) webSocketMessage).getPayload();

            if (WSConstants.PROTOCOL_SAMPLE.equals(webSocketSession.getAcceptedProtocol())) {
                WebSocketSampleProto.Sample sampleProto = WebSocketSampleProto.Sample.parseFrom(byteBuffer.array());
                log.info("sampleProto.getName():" + sampleProto.getName());
                WebSocketSampleProto.Sample newSampleProto = WebSocketSampleProto.Sample.newBuilder()
                        .setName(sampleProto + ".....server")
                        .build();

                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    newSampleProto.writeTo(byteArrayOutputStream);
                    byte[] serialized = byteArrayOutputStream.toByteArray();
                    byteBuffer = ByteBuffer.wrap(serialized);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        WebSocketMessage msg = new BinaryMessage(byteBuffer.array());
        webSocketSession.sendMessage(msg);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        log.info("Session closed Id[" + webSocketSession.getId() + "] CloseStatus:" + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {

        return false;
    }

    /**
     * Sub Protocol list
     *
     * @return
     */
    @Override
    public List<String> getSubProtocols() {

        return WSConstants.SUB_PROTOCOL_LIST;
    }
}
