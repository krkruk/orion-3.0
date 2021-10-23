package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.onboard.chassis.ChassisSerialRxTelemetryMsg;

public class ChassisNetTxTelemetryMapper implements Function<ChassisSerialRxTelemetryMsg, ChassisSerialRxTelemetryMsg> {
    @Override
    public ChassisSerialRxTelemetryMsg apply(ChassisSerialRxTelemetryMsg chassisTelemetryMessage) throws Throwable {
        return chassisTelemetryMessage;
    }
}
