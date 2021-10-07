package pl.projektorion.gateway.subscriber;

import pl.projektorion.gateway.BasicNetworkGatewayBuilder;
import pl.projektorion.gateway.SerialNetworkGatewayBuilder;
import pl.projektorion.gateway.publisher.PublisherBuilderArgs;

public interface SubscriberApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>  apply();
}
