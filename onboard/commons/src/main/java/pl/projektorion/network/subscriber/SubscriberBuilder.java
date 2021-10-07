package pl.projektorion.network.subscriber;

import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.serializer.Serdes;

import java.util.Queue;

public class SubscriberBuilder<T> {
    private final Class<T> clazz;

    private SubscriberConfig config;
    private Serdes<T> serdes;
    private Queue<T> queue;

    public SubscriberBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public SubscriberBuilder<T> withConfig(SubscriberConfig config) {
        this.config = config;
        return this;
    }

    public SubscriberBuilder<T> withSerdes(final Serdes<T> serdes) {
        this.serdes = serdes;
        return this;
    }

    public SubscriberBuilder<T> withQueue(final Queue<T> queue) {
        this.queue = queue;
        return this;
    }

    public NetworkSubscriber<T> build() {
        return new NetworkSubscriber<>(clazz, config, serdes, queue);
    }
}
