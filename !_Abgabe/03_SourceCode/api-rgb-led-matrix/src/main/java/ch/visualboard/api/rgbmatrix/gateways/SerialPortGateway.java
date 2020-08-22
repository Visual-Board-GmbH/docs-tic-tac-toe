package ch.visualboard.api.rgbmatrix.gateways;

import ch.visualboard.api.rgbmatrix.exceptions.SerialPortCouldNotBeOpenException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("c9075efa-926b-4d44-a273-f740369ce0a9")
public class SerialPortGateway
{
    private final SerialPort serialPort;

    @Autowired
    public SerialPortGateway(final SerialPort serialPort)
    {
        this.serialPort = serialPort;
    }

    public void writeByteArrayToRgbMatrix(byte[] bytes)
        throws IOException, SerialPortCouldNotBeOpenException
    {
        if (!serialPort.isOpen()) {
            serialPort.openPort();
        }

        if (!serialPort.isOpen()) {
            throw new SerialPortCouldNotBeOpenException(String.format("Port %s is not open", serialPort));
        }

        serialPort.getOutputStream().write(bytes);
        serialPort.getOutputStream().flush();
    }
}
