package ch.visualboard.api.rgbmatrix.gateways;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.visualboard.api.rgbmatrix.exceptions.SerialPortCouldNotBeOpenException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SerialPortGatewayTest
{
    @Mock
    private SerialPort mockSerialPort;

    @Mock
    private OutputStream mockOutputStream;

    @InjectMocks
    private SerialPortGateway serialPortGateway;

    private byte[] mockByteArray;

    @BeforeEach
    void setUp()
    {
        mockByteArray = "test".getBytes();
    }

    @Test
    void testIf_writeByteArrayToRgbMatrix_isSuccessful() throws IOException, SerialPortCouldNotBeOpenException
    {
        when(mockSerialPort.isOpen()).thenReturn(false).thenReturn(true);
        when(mockSerialPort.getOutputStream()).thenReturn(mockOutputStream);
        doNothing().when(mockOutputStream).write(mockByteArray);
        doNothing().when(mockOutputStream).flush();

        serialPortGateway.writeByteArrayToRgbMatrix(mockByteArray);

        verify(mockSerialPort).openPort();
        verify(mockSerialPort, times(2)).isOpen();
        verify(mockOutputStream).write(mockByteArray);
        verify(mockOutputStream).flush();
    }

    @Test
    void testIf_WriteByteArrayToRgbMatrix_failsIfPortIsNotOpen()
    {
        when(mockSerialPort.isOpen()).thenReturn(false);

        SerialPortCouldNotBeOpenException thrown = assertThrows(
            SerialPortCouldNotBeOpenException.class,
            () -> serialPortGateway.writeByteArrayToRgbMatrix(mockByteArray)
        );

        assertTrue(thrown.getMessage().contains("is not open"));
        verify(mockSerialPort).openPort();
        verify(mockSerialPort, times(2)).isOpen();
    }
}
