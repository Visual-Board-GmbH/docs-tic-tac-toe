package ch.visualboard.api.rgbmatrix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("api.serialport")
public class SerialPortConfig
{
    private String comPort;
    private int comPortNewBaudRate;
    private int comPortNewDataBits;
    private int comPortNewStopBits;
    private int comPortNewParity;
    private int comPortNewReadTimeout;
    private int comPortNewWriteTimeout;

    public String getComPort()
    {
        return comPort;
    }

    public void setComPort(final String comPort)
    {
        this.comPort = comPort;
    }

    public int getComPortNewBaudRate()
    {
        return comPortNewBaudRate;
    }

    public void setComPortNewBaudRate(final int comPortNewBaudRate)
    {
        this.comPortNewBaudRate = comPortNewBaudRate;
    }

    public int getComPortNewDataBits()
    {
        return comPortNewDataBits;
    }

    public void setComPortNewDataBits(final int comPortNewDataBits)
    {
        this.comPortNewDataBits = comPortNewDataBits;
    }

    public int getComPortNewStopBits()
    {
        return comPortNewStopBits;
    }

    public void setComPortNewStopBits(final int comPortNewStopBits)
    {
        this.comPortNewStopBits = comPortNewStopBits;
    }

    public int getComPortNewParity()
    {
        return comPortNewParity;
    }

    public void setComPortNewParity(final int comPortNewParity)
    {
        this.comPortNewParity = comPortNewParity;
    }

    public int getComPortNewReadTimeout()
    {
        return comPortNewReadTimeout;
    }

    public void setComPortNewReadTimeout(final int comPortNewReadTimeout)
    {
        this.comPortNewReadTimeout = comPortNewReadTimeout;
    }

    public int getComPortNewWriteTimeout()
    {
        return comPortNewWriteTimeout;
    }

    public void setComPortNewWriteTimeout(final int comPortNewWriteTimeout)
    {
        this.comPortNewWriteTimeout = comPortNewWriteTimeout;
    }
}
