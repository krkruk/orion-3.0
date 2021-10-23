package pl.projektorion.manipulator;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.manipulator.ManipulatorTelemetry;
import pl.projektorion.schema.onboard.manipulator.ManipulatorSerialRxTelemetryMsg;

public class ManipulatorNetTxTelemetryMapper implements Function<ManipulatorSerialRxTelemetryMsg, ManipulatorTelemetry> {

    @Override
    public ManipulatorTelemetry apply(ManipulatorSerialRxTelemetryMsg manipulatorSerialRxTelemetryMsg) throws Throwable {
        return new ManipulatorTelemetry();
    }
}
