package pl.projektorion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.publisher.PublisherConfigLoader;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfigLoader;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigLoader;
import pl.projektorion.gateway.OrionSerialNetworkGateway;
import pl.projektorion.manipulator.ManipulatorNetTxTelemetryMapper;
import pl.projektorion.manipulator.ManipulatorSerialTxMsgOpenLoopMapper;
import pl.projektorion.schema.groundcontrol.manipulator.ManipulatorCommand;
import pl.projektorion.schema.groundcontrol.manipulator.ManipulatorTelemetry;
import pl.projektorion.schema.onboard.manipulator.ManipulatorSerialRxTelemetryMsg;
import pl.projektorion.schema.onboard.manipulator.ManipulatorSerialTxMsgOpenLoop;
import pl.projektorion.serial.OrionJsonSerdes;

public class ManipulatorApp {
    private static final Logger log = LoggerFactory.getLogger(ManipulatorApp.class);

    public static void main(String[] args) throws Exception {
        final CommandLineParser cmdArgs = CommandLineParser.parse(args);
        final SerialConfig serialConfig = SerialConfigLoader.get(cmdArgs);
        final SubscriberConfig subscriberConfig = SubscriberConfigLoader.get(cmdArgs);
        final PublisherConfig publisherConfig = PublisherConfigLoader.get(cmdArgs);
        log.info("Serial props = {}", serialConfig);
        log.info("Subscriber props = {}", subscriberConfig);
        log.info("Publisher props = {}", publisherConfig);

        // Network Rx -> Serial Tx -> Serial Rx -> Network Tx
        OrionSerialNetworkGateway.builder(ManipulatorCommand.class, ManipulatorSerialTxMsgOpenLoop.class,
                        ManipulatorSerialRxTelemetryMsg.class, ManipulatorTelemetry.class)
                .basic()
                .serial()
                    .withConfig(serialConfig)
                    .withSerdes(new OrionJsonSerdes<>(ManipulatorSerialTxMsgOpenLoop.class), new OrionJsonSerdes<>(ManipulatorSerialRxTelemetryMsg.class))
                    .apply()
                .subscriber()
                    .withConfig(subscriberConfig)
                    .withSerdes(new OrionJsonSerdes<>(ManipulatorCommand.class))
                    .withMapper(new ManipulatorSerialTxMsgOpenLoopMapper())
                    .apply()
                .publisher()
                    .withConfig(publisherConfig)
                    .withSerdes(new OrionJsonSerdes<>(ManipulatorTelemetry.class))
                    .withMapper(new ManipulatorNetTxTelemetryMapper())
                    .apply()
                .build()
                .run();
    }
}
