package pl.projektorion.gateway.subscriber;

import io.reactivex.rxjava3.functions.Function;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.gateway.BasicNetworkGatewayBuilder;
import pl.projektorion.gateway.SerialNetworkGatewayBuilder;
import pl.projektorion.serializer.Serdes;

import java.util.Objects;

public class BasicSubscriberGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>
        implements SubscriberBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> {

    private final SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder;
    private SubscriberConfig config;
    private Function<NetRx, SerialTx> mapper;
    private Serdes<NetRx> rxSerdes;

    public BasicSubscriberGatewayBuilder(SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder) {
        this.rootBuilder = rootBuilder;
    }

    @Override
    public SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> apply() {
        Objects.requireNonNull(config, "SubscriberConfig must be provided!");
        Objects.requireNonNull(mapper, "Mapper must be provided in order to translate commands incoming from network into uC-compatible statements");
        Objects.requireNonNull(rxSerdes, "Rx Serdes is mandatory to receive messages from the network");
        assert (rootBuilder instanceof BasicNetworkGatewayBuilder) : "BasicSerialGatewayBuilder requires a corresponding BasicNetworkGatewayBuilder instance";
        ((BasicNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>) rootBuilder).setSubscriber(this);
        return rootBuilder;
    }

    @Override
    public SubscriberSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(SubscriberConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public SubscriberApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withMapper(Function<NetRx, SerialTx> mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public SubscriberMapperBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(Serdes<NetRx> rx) {
        this.rxSerdes = rx;
        return this;
    }

    public SubscriberConfig getConfig() {
        return config;
    }

    public Function<NetRx, SerialTx> getMapper() {
        return mapper;
    }

    public Serdes<NetRx> getRxSerdes() {
        return rxSerdes;
    }
}
