package pl.projektorion.gateway.serial;

import pl.projektorion.serial.OrionDeviceListener;

public interface SerialListenerBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    SerialApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withListenerBuilder(OrionDeviceListener.Builder<SerialRx> builder);
}
