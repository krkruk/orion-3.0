package pl.projektorion.gateway.serial;

import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.gateway.BasicNetworkGatewayBuilder;
import pl.projektorion.gateway.SerialNetworkGatewayBuilder;
import pl.projektorion.serial.OrionDeviceListener;
import pl.projektorion.serializer.Serdes;

import java.util.Objects;

public class BasicSerialGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>
        implements SerialBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> {

    private final SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder;
    private SerialConfig config;
    private Serdes<SerialTx> txSerdes;
    private Serdes<SerialRx> rxSerdes;
    private OrionDeviceListener.Builder<SerialRx> listenerBuilder;

    public BasicSerialGatewayBuilder(SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> rootBuilder) {
        this.rootBuilder = rootBuilder;
    }

    @Override
    public SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> apply() {
        Objects.requireNonNull(config, "SerialConfig must be provided");
        Objects.requireNonNull(txSerdes, "Tx Serdes is mandatory to send messages to the microcontroller");
        Objects.requireNonNull(rxSerdes, "Rx Serdes is mandatory to receive messages from the microcontroller");
        assert (rootBuilder instanceof BasicNetworkGatewayBuilder) : "BasicSerialGatewayBuilder requires a corresponding BasicNetworkGatewayBuilder instance";
        ((BasicNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx>) rootBuilder).setSerial(this);
        return rootBuilder;
    }

    @Override
    public SerialSerdesBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withConfig(SerialConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public SerialApplyBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withListenerBuilder(OrionDeviceListener.Builder<SerialRx> builder) {
        this.listenerBuilder = builder;
        return this;
    }

    @Override
    public SerialListenerBuilderArg<NetRx, SerialTx, SerialRx, NetTx> withSerdes(Serdes<SerialTx> tx, Serdes<SerialRx> rx) {
        this.txSerdes = tx;
        this.rxSerdes = rx;
        return this;
    }

    public SerialConfig getConfig() {
        return config;
    }

    public Serdes<SerialTx> getTxSerdes() {
        return txSerdes;
    }

    public Serdes<SerialRx> getRxSerdes() {
        return rxSerdes;
    }

    public OrionDeviceListener.Builder<SerialRx> getListenerBuilder() {
        return listenerBuilder;
    }
}
