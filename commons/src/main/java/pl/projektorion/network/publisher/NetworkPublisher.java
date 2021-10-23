package pl.projektorion.network.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.serializer.Serdes;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NetworkPublisher<T> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NetworkPublisher.class);

    private final Class<T> clazz;
    private final PublisherConfig config;
    private final Serdes<T> serdes;
    private final BlockingQueue<T> queue;

    private volatile boolean stopped = false;

    public NetworkPublisher(Class<T> clazz, PublisherConfig config, Serdes<T> serdes, BlockingQueue<T> queue) {
        this.clazz = clazz;
        this.config = config;
        this.serdes = serdes;
        this.queue = queue;
    }

    public synchronized void toggleStop() {
        stopped = !stopped;
        log.info("{} is about to stop.", NetworkPublisher.class.getName());
    }

    @Override
    public void run() {
        log.info("Preparing NetworkPublisher...");
        try (ZContext ctx = new ZContext()) {
            final ZMQ.Socket sender = ctx.createSocket(SocketType.PUB);
            sender.connect(config.getReceiverAddress());

            log.info("About to start sending {} messages over the network", clazz.getCanonicalName());
            while (!Thread.currentThread().isInterrupted() && !stopped) {
                T msg = poll();
                if (msg == null) {
                    continue;
                }

                try {
                    final byte[] message = serdes.serialize(msg);
                    log.debug("Sending to the upstream: {}", msg);
                    sender.send(message);
                } catch (Exception e) {
                    log.warn("Cannot send a message {} to {}", msg, config.getReceiverAddress());
                }
            }

            log.info("Publisher socket has been closed.");
        }
    }

    private T poll() {
        try {
            return queue.poll(config.getPollTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Error = {}", e.getMessage());
            return null;
        }
    }
}
