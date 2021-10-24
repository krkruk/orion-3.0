package pl.projektorion.gamepad;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerState;
import io.reactivex.rxjava3.functions.Function;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GamepadInputBuilder<T> {
    private Class<T> clazz;
    private long time;
    private TimeUnit unit;
    private int controllerIndex;
    private Function<ControllerState, T> mapper;
    private BlockingQueue<T> queue;

    GamepadInputBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public GamepadInputBuilder<T> withUpdateInterval(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
        return this;
    }

    public GamepadInputBuilder<T> withController(int index) {
        this.controllerIndex = index;
        return this;
    }

    public GamepadInputBuilder<T> withMapper(Function<ControllerState, T> mapper) {
        this.mapper = mapper;
        return this;
    }
    public GamepadInputBuilder<T> withSinkQueue(BlockingQueue<T> queue) {
        this.queue = queue;
        return this;
    }

    public GamepadInput<T> build() {
        assert time > 0 : "Time must be greater than 0 (of chosen TimeUnit)";
        Objects.requireNonNull(unit, "TimeUnit must not be null");
        Objects.requireNonNull(mapper, "Gamepad Mapper function must be provided");
        Objects.requireNonNull(queue, "Sink queue cannot be null");

        return new GamepadInput<>(time, unit, controllerIndex, mapper, queue);
    }
}
