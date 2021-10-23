package pl.projektorion.gateway;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.gateway.publisher.PublisherApplyBuilderArg;
import pl.projektorion.gateway.publisher.PublisherBuilderArgs;
import pl.projektorion.gateway.publisher.PublisherMapperBuilderArg;
import pl.projektorion.gateway.publisher.PublisherSerdesBuilderArg;
import pl.projektorion.serializer.Serdes;

import java.util.Objects;

public class BasicPublisherGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>
        implements PublisherBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> {

    private final SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder;
    private PublisherConfig config;
    private Function<SerialRx, NetTx> mapper;
    private Serdes<NetTx> txSerdes;

    public BasicPublisherGatewayBuilder(SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder) {
        this.rootBuilder = rootBuilder;
    }

    @Override
    public SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> apply() {
        Objects.requireNonNull(config, "PublisherConfig must be provided!");
        Objects.requireNonNull(mapper, "Mapper must be provided in order to translate commands incoming from uC into network-ready broadcast");
        Objects.requireNonNull(txSerdes, "Tx Serdes is mandatory to deserialize messages from the uC");
        assert (rootBuilder instanceof BasicSerialNetworkGatewayBuilder) : "BasicSerialGatewayBuilder requires a corresponding BasicNetworkGatewayBuilder instance";
        ((BasicSerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>) rootBuilder).setPublisher(this);
        return rootBuilder;
    }

    @Override
    public PublisherSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(PublisherConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public PublisherApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withMapper(Function<SerialRx, NetTx> mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public PublisherMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(Serdes<NetTx> tx) {
        this.txSerdes = tx;
        return this;
    }

    public PublisherConfig getConfig() {
        return config;
    }

    public Function<SerialRx, NetTx> getMapper() {
        return mapper;
    }

    public Serdes<NetTx> getTxSerdes() {
        return txSerdes;
    }
}
