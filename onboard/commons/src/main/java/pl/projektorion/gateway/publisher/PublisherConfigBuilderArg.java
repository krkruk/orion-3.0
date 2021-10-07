package pl.projektorion.gateway.publisher;

import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfig;

public interface PublisherConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    PublisherSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(final PublisherConfig config);
}
