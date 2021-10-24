package pl.projektorion.chassis;

import io.reactivex.rxjava3.functions.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.onboard.chassis.ChassisSerialTxMsgOpenLoop;

public class ChassisSerialTxMsgOpenLoopMapper implements Function<ChassisCommand, ChassisSerialTxMsgOpenLoop> {
    private static final Logger log = LoggerFactory.getLogger(ChassisSerialTxMsgOpenLoopMapper.class);

    private static final float MAX_PWM = 255.0f;

    @Override
    public ChassisSerialTxMsgOpenLoop apply(ChassisCommand chassisCommand) throws Throwable {
        log.trace("Received upstream message = {}. Mapping", chassisCommand);
        return new ChassisSerialTxMsgOpenLoop(
                convert(chassisCommand.getXAxis()),
                convert(chassisCommand.getYAxis()));
    }

    private static short convert(double value) {
        return (short) (MAX_PWM * value);
    }
}
