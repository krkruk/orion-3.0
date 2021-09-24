package pl.projektorion;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.hardware.chassis.ChassisCommandMessage;
import pl.projektorion.hardware.chassis.ChassisTelemetryListener;
import pl.projektorion.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.serial.OrionJsonSerdes;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);


        final BlockingQueue<ChassisCommandMessage> commands = new LinkedBlockingQueue<>();
        final ObservableQueue<ChassisTelemetryMessage> telemetry = new ObservableQueue<>();

        final OrionDevice<ChassisCommandMessage> device = OrionDevice.builder(ChassisCommandMessage.class)
                .withSerialConfig(serialConfig)
                .withCommandQueue(commands)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisCommandMessage.class))
                .withTelemetryListener(ChassisTelemetryListener.builder()
                        .withSerdes(new OrionJsonSerdes<>(ChassisTelemetryMessage.class))
                        .withQueue(telemetry)
                        .build())
                .build();

        final ScheduledExecutorService commandGenerator = Executors.newSingleThreadScheduledExecutor();
        commandGenerator.scheduleAtFixedRate(() -> {
            commands.add(new ChassisCommandMessage(10, new Random().nextInt(255)));
            log.info("Added command");
        }, 0,1, TimeUnit.SECONDS);

        commandGenerator.schedule(device::toggleStop, 10, TimeUnit.SECONDS); // automatic shutdown hook

        final Disposable telemetryObservable = telemetry.observe()
                .subscribe(e -> log.info("Result = {}", e.toString()));
        device.run();
        commandGenerator.shutdown();
        telemetryObservable.dispose();
        log.info("Total amount of received msgs = {}", telemetry.size());
    }
}
