package pl.projektorion;

import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import pl.projektorion.chassis.ChassisCommandMapperOpenLoop;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfigLoader;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.network.Network;
import pl.projektorion.network.NetworkSubscriber;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.schema.ground.control.ChassisCommand;
import pl.projektorion.schema.hardware.chassis.ChassisCommandMessageOpenLoop;
import pl.projektorion.schema.hardware.chassis.ChassisTelemetryListener;
import pl.projektorion.schema.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.serializer.Serdes;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.*;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        final SubscriberConfig subscriberConfig = SubscriberConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);
        log.info("Subscriber props = {}", subscriberConfig);

        final ExecutorService ioServices = Executors.newFixedThreadPool(2);

        final ObservableQueue<ChassisCommand> remoteCommands = new ObservableQueue<>();
        final BlockingQueue<ChassisCommandMessageOpenLoop> commands = new LinkedBlockingQueue<>();
        final ObservableQueue<ChassisTelemetryMessage> telemetry = new ObservableQueue<>();

        final OrionDevice<ChassisCommandMessageOpenLoop> device = OrionDevice.builder(ChassisCommandMessageOpenLoop.class)
                .withSerialConfig(serialConfig)
                .withCommandQueue(commands)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisCommandMessageOpenLoop.class))
                .withTelemetryListener(ChassisTelemetryListener.builder()
                        .withSerdes(new OrionJsonSerdes<>(ChassisTelemetryMessage.class))
                        .withQueue(telemetry)
                        .build())
                .build();

        final NetworkSubscriber<ChassisCommand> subscriber = Network.subscriber(ChassisCommand.class)
                .withConfig(subscriberConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisCommand.class))
                .withQueue(remoteCommands)
                .build();

//        final ScheduledExecutorService commandGenerator = runPeriodically(device, subscriber);

        final Disposable telemetryObservable = telemetry.observe()
                .subscribe(e -> log.info("Result = {}", e.toString()));

        final Disposable subscribeObservable = remoteCommands.observe()
                .map(new ChassisCommandMapperOpenLoop())
                .subscribe(commands::add);

        ioServices.submit(device);
//        ioServices.submit(subscriber);
        subscriber.run();

//        commandGenerator.shutdown();
        telemetryObservable.dispose();
        subscribeObservable.dispose();
        ioServices.shutdown();
        log.info("Total amount of received msgs = {}", telemetry.size());

    }

    private static ScheduledExecutorService runPeriodically(OrionDevice<ChassisCommandMessageOpenLoop> device,
                                                            NetworkSubscriber<ChassisCommand> subscriber) {
        final ScheduledExecutorService commandGenerator = Executors.newSingleThreadScheduledExecutor();
        commandGenerator.schedule(device::toggleStop, 10, TimeUnit.SECONDS); // automatic shutdown hook
        commandGenerator.schedule(subscriber::toggleStop, 8, TimeUnit.SECONDS); // automatic shutdown hook
        return commandGenerator;
    }
}
