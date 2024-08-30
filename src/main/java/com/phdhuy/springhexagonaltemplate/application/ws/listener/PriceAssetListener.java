package com.phdhuy.springhexagonaltemplate.application.ws.listener;

import com.phdhuy.springhexagonaltemplate.application.ws.handler.PriceWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceAssetListener {

    private final PriceWebSocketHandler webSocketHandler;

    @RabbitListener(queues = "price.client")
    public void receiveMessage(String message) {
        webSocketHandler.handleTextMessage(message);
    }
}
