package ch.visualboard.api.tictactoe.handlers;

import ch.visualboard.api.tictactoe.services.NewGameService;
import ch.visualboard.api.tictactoe.services.OngoingGameService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("1c2bd5fe-f2f0-4d72-a200-15e2128002bd")
public class MQTTMessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MQTTMessageHandler.class);

    private final OngoingGameService ongoingGameService;
    private final NewGameService newGameService;

    @Autowired
    public MQTTMessageHandler(
        final @Lazy OngoingGameService ongoingGameService,
        final @Lazy NewGameService newGameService
    )
    {
        this.ongoingGameService = ongoingGameService;
        this.newGameService = newGameService;
    }

    public void handleMessage(String topic, MqttMessage message)
    {
        if ("ttt/new_game".equalsIgnoreCase(topic)) {
            newGameService.handleGame(topic, String.valueOf(message));
        }

        if ("ttt/game".equalsIgnoreCase(topic)) {
            ongoingGameService.handleGame(topic, String.valueOf(message));
        }
    }
}
