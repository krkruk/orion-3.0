package pl.projektorion.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.serializer.Serdes;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class OrionDevice<Command> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(OrionDevice.class);

    private final Class<Command> clazz;
    private final SerialConfig config;
    private final BlockingQueue<Command> commands;
    private final Serdes<Command> commandSerdes;
    private final SerialPortMessageListener listener;
    private final SerialPort port;

    private volatile boolean stopped = false;

    OrionDevice(Class<Command> clazz,
                SerialConfig config,
                BlockingQueue<Command> commands,
                Serdes<Command> commandSerdes,
                SerialPortMessageListener listener) {
        this.clazz = clazz;
        this.config = config;
        this.commands = commands;
        this.commandSerdes = commandSerdes;
        this.listener = listener;
        this.port = Arrays.stream(SerialPort.getCommPorts())
                .filter(p -> config.getPortName().equals(p.getSystemPortName()))
                .findFirst().orElse(null);

        initializeDevice();
    }

    public static <C> OrionDeviceBuilder<C> builder(Class<C> commandClazz) {
        return new OrionDeviceBuilder<>(commandClazz);
    }

    public synchronized void toggleStop() {
        this.stopped = !stopped;
        log.info("Serial device stop request received = {}", stopped);
    }

    @Override
    public void run() {
        if (!port.openPort()) {
            throw new IllegalStateException("Could not open the port");
        }

        log.info("About to start sending {} messages to the serial device...", clazz.getCanonicalName());
        while (!Thread.currentThread().isInterrupted() && !stopped) {
            Command msg = null;
            try {
                msg = commands.poll(config.getPollTimeout(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("Error = {}", e.getMessage());
            }
            if (msg == null) {
                continue;
            }

            log.debug("About to serialize a serial message: {}", msg);
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

        port.setBaudRate(config.getBaudRate());
        port.setComPortTimeouts(config.getDefaultTimeout(), config.getReadTimeout(), config.getWriteTimeout());
        port.addDataListener(listener);

        Runtime.getRuntime().addShutdownHook(new Thread(this::gentleShutdown));
    }

    private void gentleShutdown() {
        if (port != null && port.isOpen()) {
            port.removeDataListener();
            port.closePort();
            log.info("Serial port has been closed");
        }
    }

}
