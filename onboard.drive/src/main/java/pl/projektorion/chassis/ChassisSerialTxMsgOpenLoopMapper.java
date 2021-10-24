package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.onboard.chassis.ChassisSerialTxMsgOpenLoop;

public class ChassisSerialTxMsgOpenLoopMapper implements Function<ChassisCommand, ChassisSerialTxMsgOpenLoop> {
    private static final float MAX_PWM = 255.0f;

    @Override
    public ChassisSerialTxMsgOpenLoop apply(ChassisCommand chassisCommand) throws Throwable {
        return new ChassisSerialTxMsgOpenLoop(
                convert(chassisCommand.getXAxis()),
                convert(chassisCommand.getYAxis()));
    }

    private static short convert(float value) {
        return (short) (MAX_PWM * value);
    }
}
