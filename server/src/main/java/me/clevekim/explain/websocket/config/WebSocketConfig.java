package me.clevekim.explain.websocket.config;

import me.clevekim.explain.websocket.WebSocketMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by clevekim@naver.com on 2019-05-13
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final String WS_URL_TAIL = "/ws";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(wsHandler(), WS_URL_TAIL)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler wsHandler() {

        return new WebSocketMessageHandler();
    }

}
