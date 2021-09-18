package pl.projektorion.hardware.chassis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.serial.OrionDeviceListener;
import pl.projektorion.serializer.Serdes;

public class ChassisTelemetryListener extends OrionDeviceListener<ChassisTelemetryMessage> {
    private static final Logger log = LoggerFactory.getLogger(ChassisTelemetryListener.class);


    public ChassisTelemetryListener(Serdes<ChassisTelemetryMessage> serdes) {
        super(serdes);
    }

    @Override
    protected void processMessage(ChassisTelemetryMessage incomingMessage) {
        log.info("Received telemetry = {}", incomingMessage);
    }
}
