package pl.projektorion.gateway.subscriber;

public interface SubscriberBuilderArgs<NetRx, SerialTx, SerialRx, NetTx>
        extends SubscriberConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        SubscriberSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        SubscriberMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        SubscriberApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
}
