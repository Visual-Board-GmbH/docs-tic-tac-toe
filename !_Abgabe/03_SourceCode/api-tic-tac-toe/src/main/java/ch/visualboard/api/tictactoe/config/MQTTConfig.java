package ch.visualboard.api.tictactoe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.mqtt")
public class MQTTConfig
{
    private String serverUrl;
    private String clientId;
    private String topics;

    public String getServerUrl()
    {
        return serverUrl;
    }

    public void setServerUrl(final String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(final String clientId)
    {
        this.clientId = clientId;
    }

    public String getTopics()
    {
        return topics;
    }

    public void setTopics(final String topics)
    {
        this.topics = topics;
    }
}
