package pl.projektorion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.chassis.ChassisSerialTxMsgOpenLoopMapper;
import pl.projektorion.chassis.ChassisNetTxTelemetryMapper;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.publisher.PublisherConfigLoader;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfigLoader;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.gateway.OrionSerialNetworkGateway;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.groundcontrol.chassis.ChassisTelemetry;
import pl.projektorion.schema.onboard.chassis.ChassisSerialTxMsgOpenLoop;
import pl.projektorion.schema.onboard.chassis.ChassisSerialRxTelemetryMsg;
import pl.projektorion.serial.OrionJsonSerdes;

public class DriveApp {
    private static final Logger log = LoggerFactory.getLogger(DriveApp.class);

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        final SubscriberConfig subscriberConfig = SubscriberConfigLoader.get(cmdArgs);
        final PublisherConfig publisherConfig = PublisherConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);
        log.info("Subscriber props = {}", subscriberConfig);
        log.info("Publisher props = {}", publisherConfig);

        // Network Rx -> Serial Tx -> Serial Rx -> Network Tx
        OrionSerialNetworkGateway.builder(ChassisCommand.class, ChassisSerialTxMsgOpenLoop.class,
                        ChassisSerialRxTelemetryMsg.class, ChassisTelemetry.class)
                .basic()
                .serial()
                    .withConfig(serialConfig)
                    .withSerdes(new OrionJsonSerdes<>(ChassisSerialTxMsgOpenLoop.class), new OrionJsonSerdes<>(ChassisSerialRxTelemetryMsg.class))
                    .apply()
                .subscriber()
                    .withConfig(subscriberConfig)
                    .withSerdes(new OrionJsonSerdes<>(ChassisCommand.class))
                    .withMapper(new ChassisSerialTxMsgOpenLoopMapper())
                    .apply()
                .publisher()
                    .withConfig(publisherConfig)
                    .withSerdes(new OrionJsonSerdes<>(ChassisTelemetry.class))
                    .withMapper(new ChassisNetTxTelemetryMapper())
                    .apply()
                .build()
                .run();
    }
}
