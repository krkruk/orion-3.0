package pl.projektorion.gateway.subscriber;

import pl.projektorion.gateway.SerialNetworkGatewayBuilder;

public interface SubscriberApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>  apply();
}
