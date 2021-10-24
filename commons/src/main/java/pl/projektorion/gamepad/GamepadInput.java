package pl.projektorion.gamepad;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerState;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GamepadInput<T> implements Runnable {
    private final BlockingQueue<T> queue;
    private final ConnectableObservable<T> publisher;
    private final List<Disposable> disposables;

    GamepadInput(long time, TimeUnit unit, int controllerIndex, Function<ControllerState, T> mapper, BlockingQueue<T> queue) {
        this.queue = queue;
        this.disposables = new ArrayList<>();

        final GamepadController gamepad = GamepadController.getInstance();

        publisher = Observable.interval(time, unit)
                .map(counter -> gamepad.getState(controllerIndex))
                .map(mapper)
                .publish();
    }

    public static <V> GamepadInputBuilder<V> builder(Class<V> clazz) {
        return new GamepadInputBuilder<>(clazz);
    }

    @Override
    public void run() {
        disposables.add(publisher.subscribe(queue::add));
        disposables.add(publisher.connect());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> disposables.forEach(Disposable::dispose)));
    }
}
