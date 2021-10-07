package pl.projektorion.chassis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.schema.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.serial.OrionDeviceListener;
import pl.projektorion.serializer.Serdes;

import java.util.Objects;
import java.util.Queue;

public class ChassisTelemetryListener extends OrionDeviceListener<ChassisTelemetryMessage> {
    private static final Logger log = LoggerFactory.getLogger(ChassisTelemetryListener.class);
    private final Queue<ChassisTelemetryMessage> queue;

    ChassisTelemetryListener(Serdes<ChassisTelemetryMessage> serdes, Queue<ChassisTelemetryMessage> queue) {
        super(serdes);
        this.queue = queue;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void processMessage(ChassisTelemetryMessage incomingMessage) {
        log.trace("Received telemetry = {}", incomingMessage);
        queue.add(incomingMessage);
    }


    public static class Builder {
        private Serdes<ChassisTelemetryMessage> serdes;
        private Queue<ChassisTelemetryMessage> queue;

        public Builder withSerdes(Serdes<ChassisTelemetryMessage> serdes) {
            this.serdes = serdes;
            return this;
        }

        public Builder withQueue(Queue<ChassisTelemetryMessage> queue) {
            this.queue = queue;
            return this;
        }

        public ChassisTelemetryListener build() {
            Objects.requireNonNull(serdes, "Serdes required to deserialize incoming messages");
            Objects.requireNonNull(queue, "Queue required to further process incoming data");
            return new ChassisTelemetryListener(serdes, queue);
        }
    }
}
