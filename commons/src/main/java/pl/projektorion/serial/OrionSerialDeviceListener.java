package pl.projektorion.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.schema.onboard.chassis.ChassisSerialRxTelemetryMsg;
import pl.projektorion.serializer.Serdes;

import java.util.Queue;

public class OrionSerialDeviceListener<Msg> extends OrionDeviceListener<Msg> {
    private static final Logger log = LoggerFactory.getLogger(OrionSerialDeviceListener.class);
    private final Queue<Msg> queue;

    OrionSerialDeviceListener(Serdes<Msg> serdes, Queue<Msg> queue) {
        super(serdes);
        this.queue = queue;
    }

    public static <T> Builder<T> builder(Class<T> clazz) {
        return new Builder<>(clazz);
    }

    @Override
    protected void processMessage(Msg incomingMessage) {
        log.debug("Received telemetry = {}", incomingMessage);
        queue.add(incomingMessage);
    }

    public static final class Builder<T> extends OrionDeviceListener.Builder<T> {
        private final Class<T> clazz;

        public Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public OrionDeviceListener<T> build() {
            verify();
            return new OrionSerialDeviceListener<>(serdes, queue);
        }
    }
}
