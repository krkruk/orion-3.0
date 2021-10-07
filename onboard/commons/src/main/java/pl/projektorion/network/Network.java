package pl.projektorion.network;

import pl.projektorion.network.publisher.PublisherBuilder;
import pl.projektorion.network.subscriber.SubscriberBuilder;

public class Network {
    public static <T> SubscriberBuilder<T> subscriber(Class<T> clazz) {
        return new SubscriberBuilder<>(clazz);
    }

    public static <T> PublisherBuilder<T> publisher(Class<T> clazz) {
        return new PublisherBuilder<>(clazz);
    }
}
