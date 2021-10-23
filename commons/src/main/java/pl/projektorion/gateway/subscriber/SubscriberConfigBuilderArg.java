package pl.projektorion.gateway.subscriber;

import pl.projektorion.config.network.subscriber.SubscriberConfig;

public interface SubscriberConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    SubscriberSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(final SubscriberConfig config);
}
