package pl.projektorion.gateway.serial;

import pl.projektorion.serializer.Serdes;

public interface SerialSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    SerialApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(final Serdes<SerialTx> tx, final Serdes<SerialRx> rx);
}
