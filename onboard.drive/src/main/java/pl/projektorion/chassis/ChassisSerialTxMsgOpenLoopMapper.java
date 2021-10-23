package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.onboard.chassis.ChassisSerialTxMsgOpenLoop;

public class ChassisSerialTxMsgOpenLoopMapper implements Function<ChassisCommand, ChassisSerialTxMsgOpenLoop> {

    @Override
    public ChassisSerialTxMsgOpenLoop apply(ChassisCommand chassisCommand) throws Throwable {
        return new ChassisSerialTxMsgOpenLoop(
                (short) chassisCommand.getTwist().getSpeed(),
                (short) chassisCommand.getTwist().getAngle());
    }
}
