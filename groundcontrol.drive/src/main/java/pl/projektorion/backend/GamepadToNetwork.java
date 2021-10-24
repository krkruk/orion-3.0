package pl.projektorion.backend;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.gamepad.GamepadInput;
import pl.projektorion.network.Network;
import pl.projektorion.network.publisher.NetworkPublisher;
import pl.projektorion.network.subscriber.NetworkSubscriber;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.groundcontrol.chassis.ChassisTelemetry;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.utils.QueueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GamepadToNetwork implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GamepadToNetwork.class);
    private final static int IO_THREADS = 2;

    private final ExecutorService executor;
    private final PublisherConfig publisherConfig;
    private final SubscriberConfig subscriberConfig;
    private final BiConsumer<String, ChassisCommand> uiInput;
    private final Consumer<ChassisTelemetry> uiTelemetry;
    private final BlockingQueue<ChassisCommand> senderQueue = QueueFactory.createSenderQueue();
    private final ObservableQueue<ChassisTelemetry> receiverQueue = QueueFactory.createReceiverQueue();
    private final List<Disposable> disposables = new ArrayList<>();

    private NetworkPublisher<ChassisCommand> publisher;
    private NetworkSubscriber<ChassisTelemetry> subscriber;

    public GamepadToNetwork(PublisherConfig publisherConfig,
                            SubscriberConfig subscriberConfig,
                            BiConsumer<String, ChassisCommand> uiInput,
                            Consumer<ChassisTelemetry> uiTelemetry) {
        this.executor = Executors.newFixedThreadPool(IO_THREADS);
        this.publisherConfig = publisherConfig;
        this.subscriberConfig = subscriberConfig;
        this.uiInput = uiInput;
        this.uiTelemetry = uiTelemetry;
    }

    @Override
    public void run() {
        GamepadInput.builder(ChassisCommand.class)
                .withController(0)
                .withUpdateInterval(20, TimeUnit.MILLISECONDS)
                .withMapper(state -> {
                    final float x = state.leftStickX;
                    final float y = state.leftStickY;
                    return new ChassisCommand(x, y);
                })
                .withPeek(uiInput)
                .withSinkQueue(senderQueue)
                .build()
                .run();

        publisher = Network.publisher(ChassisCommand.class)
                .withConfig(publisherConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisCommand.class))
                .withQueue(senderQueue)
                .build();

        subscriber = Network.subscriber(ChassisTelemetry.class)
                .withConfig(subscriberConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisTelemetry.class))
                .withQueue(receiverQueue)
                .build();

        disposables.add(receiverQueue.observe().subscribe(uiTelemetry));
        executor.submit(publisher);
        executor.submit(subscriber);
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
    }

    private void cleanup() {
        publisher.toggleStop();
        subscriber.toggleStop();
        disposables.forEach(Disposable::dispose);
        executor.shutdown();
    }
}
