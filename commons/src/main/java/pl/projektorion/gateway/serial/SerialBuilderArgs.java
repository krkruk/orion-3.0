package pl.projektorion.gateway.serial;

public interface SerialBuilderArgs<NetRx, SerialTx, SerialRx, NetTx>
        extends SerialConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        SerialSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        SerialApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
}
