package pl.projektorion.gateway.publisher;

import io.reactivex.rxjava3.functions.Function;

public interface PublisherMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
    PublisherApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withMapper(final Function<SerialRx, NetTx> mapper);
}
