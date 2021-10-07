package pl.projektorion.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.network.publisher.NetworkPublisher;
import pl.projektorion.network.subscriber.NetworkSubscriber;
import pl.projektorion.serial.OrionDevice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrionSerialNetworkGateway<NetRx, SerialTx, SerialRx, NetTx> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(OrionSerialNetworkGateway.class);
    private static final int NETWORK_IO_THREADS = 2;

    private final OrionDevice<SerialTx> device;
    private final NetworkSubscriber<NetRx> subscriber;
    private final NetworkPublisher<NetTx> publisher;

    private final ExecutorService ioServices;
    private final Runnable cleanup;

    OrionSerialNetworkGateway(
            final OrionDevice<SerialTx> device,
            final NetworkSubscriber<NetRx> subscriber,
            final NetworkPublisher<NetTx> publisher,
            final Runnable cleanup) {
        this.device = device;
        this.subscriber = subscriber;
        this.publisher = publisher;
        this.cleanup = cleanup;
        this.ioServices = Executors.newFixedThreadPool(NETWORK_IO_THREADS);
    }

    public static <NetRx, SerialTx, SerialRx, NetTx> OrionSerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> builder(
            Class<NetRx> netRxClass, Class<SerialTx> serialTxClass, Class<SerialRx> serialRxClass, Class<NetTx> netTxClass) {
        return new OrionSerialNetworkGatewayBuilder<>(netRxClass, serialTxClass, serialRxClass, netTxClass);
    }

    @Override
    public void run() {
        ioServices.submit(subscriber);
        ioServices.submit(publisher);

        device.run();
        cleanup();
        log.info("Shutdown complete.");
    }

    private void cleanup() {
        subscriber.toggleStop();
        publisher.toggleStop();
        cleanup.run();
        ioServices.shutdown();
    }

    public static class OrionSerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> {
        private final Class<NetRx> netRxClass;
        private final Class<SerialTx> serialTxClass;
        private final Class<SerialRx> serialRxClass;
        private final Class<NetTx> netTxClass;

        OrionSerialNetworkGatewayBuilder(Class<NetRx> netRxClass, Class<SerialTx> serialTxClass, Class<SerialRx> serialRxClass, Class<NetTx> netTxClass) {
            this.netRxClass = netRxClass;
            this.serialTxClass = serialTxClass;
            this.serialRxClass = serialRxClass;
            this.netTxClass = netTxClass;
        }

        public BasicNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> basic() {
            return new BasicNetworkGatewayBuilder<>(netRxClass, serialTxClass, serialRxClass, netTxClass);
        }

        public SerialNetworkGatewayBuilder<NetRx, SerialTx, SerialRx, NetTx> customizable() {
            throw new UnsupportedOperationException("Customizable isn't supported yet");
        }
    }
}
