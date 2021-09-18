package pl.projektorion.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.serializer.Serdes;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class OrionDevice<Command> {
    private static final Logger log = LoggerFactory.getLogger(OrionDevice.class);
    private static final String portName = "ttyACM0";

    private final BlockingQueue<Command> commands;
    private final Serdes<Command> commandSerdes;
    private final SerialPortMessageListener listener;
    private final SerialPort port;

    private volatile boolean stopped = false;

    OrionDevice(BlockingQueue<Command> commands, Serdes<Command> commandSerdes, SerialPortMessageListener listener) {
        this.commands = commands;
        this.commandSerdes = commandSerdes;
        this.listener = listener;
        this.port = Arrays.stream(SerialPort.getCommPorts())
                .filter(p -> portName.equals(p.getSystemPortName()))
                .findFirst().orElse(null);

        initializeDevice();
    }

    public static <C> OrionDeviceBuilder<C> builder(Class<C> commandClazz) {
        return new OrionDeviceBuilder<>(commandClazz);
    }

    public void setStopped(boolean stopped) {
        log.info("Serial device stop request received = {}", stopped);
        this.stopped = stopped;
    }

    public void run() {
        if (!port.openPort()) {
            throw new IllegalStateException("Could not open the port");
        }

        log.info("Running device...");
        while (!stopped) {
            Command msg = null;
            try {
                msg = commands.poll(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Serializing = {}", msg);
            try {
                final byte[] serialized = commandSerdes.serialize(msg);
                port.writeBytes(serialized, serialized.length);
            } catch (Exception e) {
                log.warn("Cannot serialize msg = {}", msg);
            }
        }

        gentleShutdown();
    }

    private void initializeDevice() {
        if (port == null) {
            throw new IllegalStateException("No port detected");
        }

        port.setBaudRate(115200);
        port.setComPortTimeouts(200, 100, 100);
        port.addDataListener(listener);

        Runtime.getRuntime().addShutdownHook(new Thread(this::gentleShutdown));
    }

    private void gentleShutdown() {
        if (port != null) {
            port.removeDataListener();
            port.closePort();
            log.info("Serial port has been closed");
        }
    }

}
