package pl.projektorion.gateway.serial;

import pl.projektorion.gateway.SerialNetworkGatewayBuilder;

public interface SerialApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> apply();
}
