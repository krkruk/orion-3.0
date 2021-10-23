package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.ChassisCommand;
import pl.projektorion.schema.onboard.chassis.ChassisCommandMessageOpenLoop;

public class ChassisSerialTxOpenLoopCommandMapper implements Function<ChassisCommand, ChassisCommandMessageOpenLoop> {

    @Override
    public ChassisCommandMessageOpenLoop apply(ChassisCommand chassisCommand) throws Throwable {
        return new ChassisCommandMessageOpenLoop(
                (short) chassisCommand.getTwist().getSpeed(),
                (short) chassisCommand.getTwist().getAngle());
    }
}
