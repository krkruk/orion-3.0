package pl.projektorion.manipulator;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.schema.groundcontrol.manipulator.ManipulatorCommand;
import pl.projektorion.schema.onboard.manipulator.ManipulatorSerialTxMsgOpenLoop;

public class ManipulatorSerialTxMsgOpenLoopMapper implements Function<ManipulatorCommand, ManipulatorSerialTxMsgOpenLoop> {
    @Override
    public ManipulatorSerialTxMsgOpenLoop apply(ManipulatorCommand manipulatorCommand) throws Throwable {
        return new ManipulatorSerialTxMsgOpenLoop();
    }
}
