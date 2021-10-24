package pl.projektorion.chassis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.chassis.ChassisTelemetry;
import pl.projektorion.schema.onboard.chassis.ChassisSerialRxTelemetryMsg;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisNetTxTelemetryMapper implements Function<ChassisSerialRxTelemetryMsg, ChassisTelemetry> {
    @Override
    public ChassisTelemetry apply(ChassisSerialRxTelemetryMsg msg) throws Throwable {
        return new ChassisTelemetry(
                msg.getLeftFrontPwm(),
                msg.getRightFrontPwm(),
                msg.getLeftRearPwm(),
                msg.getRightRearPwm(),
                msg.getErrorCode(),
                msg.getErrorDescription()
        );
    }
}
