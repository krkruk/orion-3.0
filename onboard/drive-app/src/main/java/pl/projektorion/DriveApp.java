package pl.projektorion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.hardware.chassis.ChassisCommandMessage;
import pl.projektorion.hardware.chassis.ChassisTelemetryListener;
import pl.projektorion.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.serial.OrionJsonSerdes;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);
    private static final String portName = "ttyACM0";

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);


        final LinkedBlockingQueue<ChassisCommandMessage> commands = new LinkedBlockingQueue<>();

        final OrionDevice<ChassisCommandMessage> device = OrionDevice.builder(ChassisCommandMessage.class)
                .withSerialConfig(serialConfig)
                .withCommandQueue(commands)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisCommandMessage.class))
                .withTelemetryListener(new ChassisTelemetryListener(new OrionJsonSerdes<>(ChassisTelemetryMessage.class)))
                .build();

        final ScheduledExecutorService commandGenerator = Executors.newSingleThreadScheduledExecutor();
        commandGenerator.scheduleAtFixedRate(() -> {
            commands.add(new ChassisCommandMessage(10, new Random().nextInt(255)));
            log.info("Added command");
        }, 0,1, TimeUnit.SECONDS);

        commandGenerator.schedule(device::toggleStop, 10, TimeUnit.SECONDS); // automatic shutdown hook

        device.run();
        commandGenerator.shutdown();
    }
}
