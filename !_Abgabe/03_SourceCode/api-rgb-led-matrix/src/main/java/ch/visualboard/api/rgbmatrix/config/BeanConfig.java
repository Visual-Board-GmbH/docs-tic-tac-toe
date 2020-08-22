package ch.visualboard.api.rgbmatrix.config;

import ch.visualboard.api.rgbmatrix.gateways.SerialPortGateway;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig
{
    private final SerialPortConfig serialPortConfig;

    public BeanConfig(final SerialPortConfig serialPortConfig)
    {
        this.serialPortConfig = serialPortConfig;
    }

    @Bean
    public SerialPort serialPort()
    {
        SerialPort serialPort = SerialPort.getCommPort(serialPortConfig.getComPort());

        serialPort.setComPortParameters(
            serialPortConfig.getComPortNewBaudRate(),
            serialPortConfig.getComPortNewDataBits(),
            serialPortConfig.getComPortNewStopBits(),
            serialPortConfig.getComPortNewParity()
        ); // default connection settings for RaspberryPi

        serialPort.setComPortTimeouts(
            SerialPort.TIMEOUT_WRITE_BLOCKING,
            serialPortConfig.getComPortNewReadTimeout(),
            serialPortConfig.getComPortNewWriteTimeout()
        ); // block until bytes can be written

        return serialPort;
    }

    @Bean
    public SerialPortGateway serialPortGateway()
    {
        return new SerialPortGateway(serialPort());
    }
}
