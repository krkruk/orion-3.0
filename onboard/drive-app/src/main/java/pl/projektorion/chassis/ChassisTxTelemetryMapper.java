package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.hardware.chassis.ChassisTelemetryMessage;

public class ChassisTxTelemetryMapper implements Function<ChassisTelemetryMessage, ChassisTelemetryMessage> {
    @Override
    public ChassisTelemetryMessage apply(ChassisTelemetryMessage chassisTelemetryMessage) throws Throwable {
        return chassisTelemetryMessage;
    }
}
