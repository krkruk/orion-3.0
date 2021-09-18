package pl.projektorion.serial;

import com.fazecast.jSerialComm.SerialPortMessageListener;
import pl.projektorion.serializer.Serdes;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class OrionDeviceBuilder<Command> {

    private final Class<Command> cmdClazz;
    private BlockingQueue<Command> commands;
    private Serdes<Command> commandSerdes;

    private SerialPortMessageListener listener;

    public OrionDeviceBuilder(Class<Command> cmdClazz) {
        this.cmdClazz = cmdClazz;
    }


    public OrionDeviceBuilder<Command> withCommandQueue(final BlockingQueue<Command> commands) {
        this.commands = commands;
        return this;
    }

    public OrionDeviceBuilder<Command> withCommandSerdes(final Serdes<Command> commandSerdes) {
        this.commandSerdes = commandSerdes;
        return this;
    }

    public OrionDeviceBuilder<Command> withTelemetryListener(SerialPortMessageListener listener) {
        this.listener = listener;
        return this;
    }


    public OrionDevice<Command> build() {
        return new OrionDevice<>(commands, commandSerdes, listener);
    }
}
