package pl.projektorion.gateway.subscriber;

import pl.projektorion.serializer.Serdes;

public interface SubscriberSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    SubscriberMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(final Serdes<NetRx> rx);
}
