package org.antonakospanos.iot.atlas.web.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.antonakospanos.iot.atlas.service.EventsService;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatResponseData;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatSuccessResponse;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


public class DeviceWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(DeviceWebSocketHandler.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    EventsService service;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("Opened new session in instance " + this);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Parse input
        HeartbeatRequest jsonInput = objectMapper.readValue(message.getPayload(), HeartbeatRequest.class);
        HeartbeatResponseData data = service.create(jsonInput);

        // Send output
        HeartbeatSuccessResponse heartbeatSuccessResponse = HeartbeatSuccessResponse.Builder().build(Result.SUCCESS).data(data);
        String jsonOut = objectMapper.writeValueAsString(heartbeatSuccessResponse);
        session.sendMessage(new TextMessage(jsonOut));
    }
}