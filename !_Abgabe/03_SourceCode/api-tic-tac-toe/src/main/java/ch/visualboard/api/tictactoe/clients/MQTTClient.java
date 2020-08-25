package ch.visualboard.api.tictactoe.clients;

import ch.visualboard.api.tictactoe.ApiTicTacToeApplication;
import ch.visualboard.api.tictactoe.config.MQTTConfig;
import ch.visualboard.api.tictactoe.handlers.MQTTMessageHandler;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("e91344e7-166e-4cc2-9886-059be5edfe3b")
public class MQTTClient implements MqttCallback
{
    private static final Logger logger = LoggerFactory.getLogger(MQTTClient.class);

    private final MQTTConfig mqttConfig;
    private final MQTTMessageHandler mqttMessageHandler;

    private MqttClient mqttClient;

    @Autowired
    public MQTTClient(
        final MQTTConfig mqttConfig,
        final MQTTMessageHandler mqttMessageHandler
    )
    {
        this.mqttConfig = mqttConfig;
        this.mqttMessageHandler = mqttMessageHandler;
    }

    public void sendMessage(final String topic, final String message)
    {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            mqttMessage.setQos(2);
            mqttClient.publish(topic, mqttMessage);
        }
        catch (MqttException e) {
            logger.error("The MQTT message could not be sent.", e);
        }
    }

    @Override
    public void connectionLost(final Throwable throwable)
    {
        try {

            ApiTicTacToeApplication.restart();
        }
        catch (Exception e) {
            logger.error("The connection to the MQTT Broker could not be established.", e);
        }
    }

    @Override
    public void messageArrived(final String topic, final MqttMessage message)
    {
        mqttMessageHandler.handleMessage(topic, message);
    }

    @Override
    public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken)
    {
        // This method is not relevant but is used here because of the implemented class
    }

    public void init() throws MqttException
    {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setConnectionTimeout(100000);
        mqttConnectOptions.setAutomaticReconnect(false);

        MqttClient mqttClient = new MqttClient(mqttConfig.getServerUrl(), mqttConfig.getClientId());
        mqttClient.connect(mqttConnectOptions);
        mqttClient.setTimeToWait(100);
        mqttClient.setCallback(this);

        for (String topic : StringUtils.commaDelimitedListToSet(mqttConfig.getTopics())) {
            mqttClient.subscribe(topic);
        }

        setMqttClient(mqttClient);
    }

    private void setMqttClient(final MqttClient mqttClient)
    {
        this.mqttClient = mqttClient;
    }
}
