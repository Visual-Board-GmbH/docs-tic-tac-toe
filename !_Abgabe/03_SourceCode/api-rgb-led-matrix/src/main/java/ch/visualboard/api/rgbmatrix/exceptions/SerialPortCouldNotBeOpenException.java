package ch.visualboard.api.rgbmatrix.exceptions;

public class SerialPortCouldNotBeOpenException extends Exception
{
    private static final long serialVersionUID = 3700534902228477736L;

    public SerialPortCouldNotBeOpenException(final String message)
    {
        super(message);
    }

    public SerialPortCouldNotBeOpenException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public SerialPortCouldNotBeOpenException(final Throwable cause)
    {
        super(cause);
    }
}
