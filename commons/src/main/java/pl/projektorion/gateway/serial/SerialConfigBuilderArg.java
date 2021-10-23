package pl.projektorion.gateway.serial;

import pl.projektorion.config.serial.SerialConfig;

public interface SerialConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    SerialSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(final SerialConfig config);
}
