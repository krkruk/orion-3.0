package pl.projektorion.gateway;

import pl.projektorion.gateway.publisher.PublisherBuilderArgs;
import pl.projektorion.gateway.serial.SerialBuilderArgs;
import pl.projektorion.gateway.subscriber.SubscriberBuilderArgs;

public interface SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> {
    SerialBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> serial();
    SubscriberBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> subscriber();
    PublisherBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> publisher();
    Runnable build();
}
