package pl.projektorion.network;

public class Network {
    public static <T> SubscriberBuilder<T> subscriber(Class<T> clazz) {
        return new SubscriberBuilder<>(clazz);
    }
}
