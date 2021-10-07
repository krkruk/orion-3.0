package pl.projektorion.gateway;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.gateway.publisher.PublisherBuilderArgs;
import pl.projektorion.gateway.serial.SerialBuilderArgs;
import pl.projektorion.gateway.subscriber.SubscriberBuilderArgs;
import pl.projektorion.network.Network;
import pl.projektorion.network.publisher.NetworkPublisher;
import pl.projektorion.network.subscriber.NetworkSubscriber;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.utils.QueueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BasicSerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> implements SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> {
    private static final Logger log = LoggerFactory.getLogger(BasicSerialNetworkGatewayBuilder.class);

    private final Class<NetRx> netRxClass;
    private final Class<SerialTx> serialTxClass;
    private final Class<SerialRx> serialRxClass;
    private final Class<NetTx> netTxClass;

    private BasicSerialGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> serial;
    private BasicSubscriberGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> sub;
    private BasicPublisherGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> pub;

    private final ObservableQueue<NetRx> commandReceiver = QueueFactory.createReceiverQueue();
    private final BlockingQueue<SerialTx> commandSender = QueueFactory.createSenderQueue();
    private final ObservableQueue<SerialRx> telemetryReceiver = QueueFactory.createReceiverQueue();
    private final BlockingQueue<NetTx> telemetrySender = QueueFactory.createSenderQueue();

    private final List<Disposable> disposables;

    BasicSerialNetworkGatewayBuilder(Class<NetRx> netRxClass, Class<SerialTx> serialTxClass, Class<SerialRx> serialRxClass, Class<NetTx> netTxClass) {
        this.netRxClass = netRxClass;
        this.serialTxClass = serialTxClass;
        this.serialRxClass = serialRxClass;
        this.netTxClass = netTxClass;
        this.disposables = new ArrayList<>();
    }

    @Override
    public SerialBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> serial() {
        return new BasicSerialGatewayBuilder<>(this);
    }

    @Override
    public SubscriberBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> subscriber() {
        return new BasicSubscriberGatewayBuilder<>(this);
    }

    @Override
    public PublisherBuilderArgs<NetRx, SerialTx, SerialRx, NetTx> publisher() {
        return new BasicPublisherGatewayBuilder<>(this);
    }

    @Override
    public Runnable build() {
        final OrionDevice<SerialTx> device = OrionDevice.builder(serialTxClass)
                .withSerialConfig(serial.getConfig())
                .withCommandQueue(commandSender)
                .withCommandSerdes(serial.getTxSerdes())
                .withTelemetryListener(serial.getListenerBuilder()
                        .withSerdes(serial.getRxSerdes())
                        .withQueue(telemetryReceiver)
                        .build())
                .build();

        final NetworkSubscriber<NetRx> subscriber = Network.subscriber(netRxClass)
                .withConfig(sub.getConfig())
                .withSerdes(sub.getRxSerdes())
                .withQueue(commandReceiver)
                .build();

        final NetworkPublisher<NetTx> publisher = Network.publisher(netTxClass)
                .withConfig(pub.getConfig())
                .withSerdes(pub.getTxSerdes())
                .withQueue(telemetrySender)
                .build();

        connectPipelines();
        return new OrionSerialNetworkGateway<>(device, subscriber, publisher, this::cleanup);
    }

    private void connectPipelines() {
        final ConnectableObservable<SerialRx> publisherObservable = telemetryReceiver.observe().publish();
        disposables.add(publisherObservable.subscribe(e -> log.info("Received telemetry from the uC: {}", e.toString())));

        disposables.add(publisherObservable
                .map(pub.getMapper())
                .subscribe(telemetrySender::add));
        disposables.add(commandReceiver.observe()
                .map(sub.getMapper())
                .subscribe(commandSender::add));

        disposables.add(publisherObservable.connect());
    }

    private void cleanup() {
        disposables.forEach(Disposable::dispose);
    }

    void setSerial(BasicSerialGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> serial) {
        this.serial = serial;
    }

    void setSubscriber(BasicSubscriberGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> subscriber) {
        this.sub = subscriber;
    }

    void setPublisher(BasicPublisherGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> publisher) {
        this.pub = publisher;
    }
}
