package pl.projektorion;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.chassis.ChassisCommandMapperOpenLoop;
import pl.projektorion.chassis.ChassisTelemetryListener;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.publisher.PublisherConfigLoader;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfigLoader;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.network.Network;
import pl.projektorion.network.publisher.NetworkPublisher;
import pl.projektorion.network.subscriber.NetworkSubscriber;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.schema.ground.control.ChassisCommand;
import pl.projektorion.schema.hardware.chassis.ChassisCommandMessageOpenLoop;
import pl.projektorion.schema.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.utils.QueueFactory;

import java.util.concurrent.*;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        final SubscriberConfig subscriberConfig = SubscriberConfigLoader.get(cmdArgs);
        final PublisherConfig publisherConfig = PublisherConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);
        log.info("Subscriber props = {}", subscriberConfig);

        final ExecutorService ioServices = Executors.newFixedThreadPool(3);

        final ObservableQueue<ChassisCommand> commandReceiver = QueueFactory.createReceiverQueue();
        final BlockingQueue<ChassisCommandMessageOpenLoop> commandSender = QueueFactory.createSenderQueue();
        final ObservableQueue<ChassisTelemetryMessage> telemetryReceiver = QueueFactory.createReceiverQueue();
        final BlockingQueue<ChassisTelemetryMessage> telemetrySender = QueueFactory.createSenderQueue();

        final OrionDevice<ChassisCommandMessageOpenLoop> device = OrionDevice.builder(ChassisCommandMessageOpenLoop.class)
                .withSerialConfig(serialConfig)
                .withCommandQueue(commandSender)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisCommandMessageOpenLoop.class))
                .withTelemetryListener(ChassisTelemetryListener.builder()
                        .withSerdes(new OrionJsonSerdes<>(ChassisTelemetryMessage.class))
                        .withQueue(telemetryReceiver)
                        .build())
                .build();

        final NetworkSubscriber<ChassisCommand> subscriber = Network.subscriber(ChassisCommand.class)
                .withConfig(subscriberConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisCommand.class))
                .withQueue(commandReceiver)
                .build();

        final NetworkPublisher<ChassisTelemetryMessage> publisher = Network.publisher(ChassisTelemetryMessage.class)
                .withConfig(publisherConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisTelemetryMessage.class))
                .withQueue(telemetrySender)
                .build();

//        final ScheduledExecutorService commandGenerator = runPeriodically(device, subscriber);

        final ConnectableObservable<ChassisTelemetryMessage> publisherObservable = telemetryReceiver.observe().publish();
        final Disposable localTelemetry = publisherObservable.subscribe(e -> log.info("Result = {}", e.toString()));
        final Disposable remoteTelemetry = publisherObservable.subscribe(telemetrySender::add);
        final Disposable connect = publisherObservable.connect();

        final Disposable subscribeObservable = commandReceiver.observe()
                .map(new ChassisCommandMapperOpenLoop())
                .subscribe(commandSender::add);

        ioServices.submit(subscriber);
        ioServices.submit(publisher);

        device.run();

//        commandGenerator.shutdown();
        remoteTelemetry.dispose();
        localTelemetry.dispose();
        connect.dispose();
        subscribeObservable.dispose();
        ioServices.shutdown();
    }

    private static ScheduledExecutorService runPeriodically(OrionDevice<ChassisCommandMessageOpenLoop> device,
                                                            NetworkSubscriber<ChassisCommand> subscriber) {
        final ScheduledExecutorService commandGenerator = Executors.newSingleThreadScheduledExecutor();
        commandGenerator.schedule(device::toggleStop, 10, TimeUnit.SECONDS); // automatic shutdown hook
        commandGenerator.schedule(subscriber::toggleStop, 8, TimeUnit.SECONDS); // automatic shutdown hook
        return commandGenerator;
    }
}
