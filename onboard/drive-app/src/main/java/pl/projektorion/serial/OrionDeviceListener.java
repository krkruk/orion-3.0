package pl.projektorion.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.serializer.Serdes;

public abstract class OrionDeviceListener<Msg> implements SerialPortMessageListener {
    private static final Logger log = LoggerFactory.getLogger(OrionDeviceListener.class);
    private final Serdes<Msg> serdes;

    public OrionDeviceListener(Serdes<Msg> serdes) {
        this.serdes = serdes;
    }

    @Override
    public byte[] getMessageDelimiter() {
        return new byte[] { (byte)'\n'};
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        final byte[] line = event.getReceivedData();
        try {
            log.trace("Received serial {} message = {}", event.getSerialPort(), new String(line));
            final Msg message = serdes.deserialize(line);
            processMessage(message);
        } catch (Exception e) {
            log.error("Could not parse incoming message because of {}", e.getMessage());
        }
    }

    protected abstract void processMessage(final Msg incomingMessage);
}
