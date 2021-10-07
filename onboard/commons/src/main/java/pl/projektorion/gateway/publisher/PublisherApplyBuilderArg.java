package pl.projektorion.gateway.publisher;

import pl.projektorion.gateway.SerialNetworkGatewayBuilder;

public interface PublisherApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> apply();
}
