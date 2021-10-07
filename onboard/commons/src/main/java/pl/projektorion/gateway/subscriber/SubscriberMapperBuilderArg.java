package pl.projektorion.gateway.subscriber;

import io.reactivex.rxjava3.functions.Function;

public interface SubscriberMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {

    SubscriberApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withMapper(final Function<NetRx, SerialTx> mapper);
}
