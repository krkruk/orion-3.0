package pl.projektorion.utils;

import pl.projektorion.rx.utils.ObservableQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class QueueFactory {

    public static <T> ObservableQueue<T> createReceiverQueue() {
        return new ObservableQueue<>();
    }

    public static <T> BlockingQueue<T> createSenderQueue() {
        return new LinkedBlockingQueue<>();
    }
}
