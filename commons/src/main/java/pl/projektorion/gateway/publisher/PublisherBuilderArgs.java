package pl.projektorion.gateway.publisher;

public interface PublisherBuilderArgs<NetRx, SerialTx, SerialRx, NetTx>
        extends PublisherConfigBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        PublisherSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        PublisherMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx>,
        PublisherApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> {
}
