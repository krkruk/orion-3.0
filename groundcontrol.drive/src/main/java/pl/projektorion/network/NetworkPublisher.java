package pl.projektorion.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import pl.projektorion.hardware.controller.keyboard.WSADObserver;

public class NetworkPublisher implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NetworkPublisher.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final WSADObserver observer;

    private volatile boolean stopped = false;

    public NetworkPublisher(WSADObserver observer) {
        this.observer = observer;
    }

    public synchronized void toggleStopped() {
        stopped = !stopped;
        log.info("Published to be closed = {}", stopped);
    }

    @Override
    public void run() {
        log.info("Initializing {}", NetworkPublisher.class.getName());
        try (final ZContext ctx = new ZContext()) {
            final ZMQ.Socket inputsPublisher = ctx.createSocket(SocketType.PUB);
            inputsPublisher.connect("tcp://*:5000");

            while (!Thread.currentThread().isInterrupted() && !stopped) {
                try {
                    final String roverInputs = mapper.writeValueAsString(observer.state());
                    inputsPublisher.send(roverInputs);
                    log.info("Sent = {}", roverInputs);
                    Thread.sleep(50);
                }
                catch (Exception e) {
                    log.error("Network exception = {}", e.getMessage());
                }
            }
        }
    }
}
