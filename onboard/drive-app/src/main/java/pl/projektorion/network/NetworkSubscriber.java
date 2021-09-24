package pl.projektorion.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.schema.ground.control.ChassisCommand;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.serializer.Serdes;

import java.nio.charset.StandardCharsets;
import java.util.Queue;

public class NetworkSubscriber<T> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NetworkSubscriber.class);

    private final Class<T> clazz;
    private final SubscriberConfig config;
    private final Serdes<T> serdes;
    private final Queue<T> queue;

    private volatile boolean stopped = false;
    public NetworkSubscriber(Class<T> clazz, SubscriberConfig config, Serdes<T> serdes, Queue<T> queue) {
        this.clazz = clazz;
        this.config = config;
        this.serdes = serdes;
        this.queue = queue;
    }

    public synchronized void toggleStop() {
        stopped = !stopped;
        log.info("{} is about to stop.", NetworkSubscriber.class.getName());
    }

    @Override
    public void run() {
        try (ZContext ctx = new ZContext()) {
            final ZMQ.Socket receiver = ctx.createSocket(SocketType.SUB);
            receiver.bind(config.getBindAddress());
            receiver.subscribe(config.getTopic());
            receiver.setReceiveTimeOut(config.getReadTimeout());

            while (!Thread.currentThread().isInterrupted() && !stopped) {
                final String commandMsg = receiver.recvStr();
                // process
                if (commandMsg != null) {
                    log.trace("Received remote message = {}", commandMsg);
                    try {
                        final T deserialize = serdes.deserialize(commandMsg.getBytes(StandardCharsets.UTF_8));
                        queue.add(deserialize);
                    } catch (Exception e) {
                        log.error("Could not parse remote message because of = {}", e.getMessage());
                    }
                }
            }

            log.info("Subscriber socket has been closed.");
        }
    }
}
