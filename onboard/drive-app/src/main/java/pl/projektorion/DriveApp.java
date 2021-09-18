package pl.projektorion;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.hardware.chassis.ChassisCommandMessage;
import pl.projektorion.hardware.chassis.ChassisTelemetryListener;
import pl.projektorion.hardware.chassis.ChassisTelemetryMessage;
import pl.projektorion.serial.*;

import java.util.Random;
import java.util.concurrent.*;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);
    private static final String portName = "ttyACM0";

    public static void main(String[] args) throws Exception {
        final LinkedBlockingQueue<ChassisCommandMessage> commands = new LinkedBlockingQueue<>();

        final OrionDevice<ChassisCommandMessage> device = OrionDevice.builder(ChassisCommandMessage.class)
                .withCommandQueue(commands)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisCommandMessage.class))
                .withTelemetryListener(new ChassisTelemetryListener(new OrionJsonSerdes<>(ChassisTelemetryMessage.class)))
                .build();

        final ScheduledExecutorService commandGenerator = Executors.newSingleThreadScheduledExecutor();
        commandGenerator.scheduleAtFixedRate(() -> {
            commands.add(new ChassisCommandMessage(10, new Random().nextInt(255)));
            log.info("Added command");
        }, 0,1, TimeUnit.SECONDS);

//        commandGenerator.schedule(() -> device.setStopped(true), 10, TimeUnit.SECONDS); // automatic shutdown hook

        device.run();
        commandGenerator.shutdown();
    }
}
