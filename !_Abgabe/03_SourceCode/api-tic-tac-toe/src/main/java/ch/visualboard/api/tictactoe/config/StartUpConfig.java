package ch.visualboard.api.tictactoe.config;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("startUpConfig")
public class StartUpConfig
{
    private final MQTTClient mqttClient;

    @Autowired
    public StartUpConfig(final MQTTClient mqttClient)
    {
        this.mqttClient = mqttClient;
    }

    @PostConstruct
    public void init() throws Exception
    {
        mqttClient.init();
    }
}
