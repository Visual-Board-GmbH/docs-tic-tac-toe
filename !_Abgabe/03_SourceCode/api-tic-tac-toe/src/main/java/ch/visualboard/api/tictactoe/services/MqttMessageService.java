package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("676b8787-ef58-42fd-9c32-5aeb883333d2")
public class MqttMessageService
{
    private static final Logger logger = LoggerFactory.getLogger(MqttMessageService.class);

    private final MQTTClient mqttClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MqttMessageService(
        final MQTTClient mqttClient,
        final ObjectMapper objectMapper
    )
    {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
    }

    public void sendValidOngoingGameByTopic(OngoingGameDTO ongoingGameDTO, String topic)
    {
        sendOngoingGameByTopic(ongoingGameDTO, topic, 200);
    }

    public void sendInvalidOngoingGameByTopic(OngoingGameDTO ongoingGameDTO, String topic)
    {
        sendOngoingGameByTopic(ongoingGameDTO, topic, 5000);
    }

    private void sendOngoingGameByTopic(OngoingGameDTO ongoingGameDTO, String topic, int statusCode)
    {
        ongoingGameDTO.setStatusCode(statusCode);
        ongoingGameDTO.setServerResponse(true);
        try {
            mqttClient.sendMessage(topic, objectMapper.writeValueAsString(ongoingGameDTO));
        }
        catch (JsonProcessingException e) {
            logger.error("Could not send MQTT message", e);
        }
    }
}
