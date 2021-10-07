package pl.projektorion.gateway.publisher;

import pl.projektorion.serializer.Serdes;

public interface PublisherSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    PublisherMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(final Serdes<NetTx> tx);
}
