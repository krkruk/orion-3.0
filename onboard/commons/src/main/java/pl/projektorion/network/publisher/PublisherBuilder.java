package pl.projektorion.network.publisher;

import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.serializer.Serdes;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class PublisherBuilder<T> {
    private final Class<T> clazz;

    private PublisherConfig config;
    private Serdes<T> serdes;
    private BlockingQueue<T> queue;

    public PublisherBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public PublisherBuilder<T> withConfig(PublisherConfig config) {
        this.config = config;
        return this;
    }

    public PublisherBuilder<T> withSerdes(final Serdes<T> serdes) {
        this.serdes = serdes;
        return this;
    }

    public PublisherBuilder<T> withQueue(final BlockingQueue<T> queue) {
        this.queue = queue;
        return this;
    }

    public NetworkPublisher<T> build() {
        return new NetworkPublisher<>(clazz, config, serdes, queue);
    }
}
