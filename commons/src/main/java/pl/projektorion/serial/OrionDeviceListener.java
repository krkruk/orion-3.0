package pl.projektorion.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.serializer.Serdes;

import java.util.Objects;
import java.util.Queue;

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
            log.debug("Received serial {} message = {}", event.getSerialPort(), new String(line));
            final Msg message = serdes.deserialize(line);
            processMessage(message);
        } catch (Exception e) {
            log.error("Could not parse incoming message because of {}", e.getMessage());
        }
    }

    protected abstract void processMessage(final Msg incomingMessage);


    public static class Builder<Msg> {
        protected Serdes<Msg> serdes;
        protected Queue<Msg> queue;

        public Builder<Msg> withSerdes(Serdes<Msg> serdes) {
            this.serdes = serdes;
            return this;
        }

        public Builder<Msg> withQueue(Queue<Msg> queue) {
            this.queue = queue;
            return this;
        }

        public OrionDeviceListener<Msg> build() {
            verify();
            throw new UnsupportedOperationException("Listener builder hasn't been implemented");
        }

        protected void verify() {
            Objects.requireNonNull(serdes, "Serdes required to deserialize incoming messages");
            Objects.requireNonNull(queue, "Queue required to further process incoming data");
        }
    }
}
