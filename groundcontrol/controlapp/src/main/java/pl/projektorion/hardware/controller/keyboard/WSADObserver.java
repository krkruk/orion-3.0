package pl.projektorion.hardware.controller.keyboard;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WSADObserver {

    private static final int UPPER_BOUND = 255;
    private static final int LOWER_BOUND = -255;
    private static final int USER_STEP = 10;
    private static final int DECREMENT_STEP = 60;
    private static final long DECREMENT_AFTER_INACTIVITY = 500;

    private int ahead;
    private int sideways;
    private Instant lastPressed;

    public WSADObserver(ScheduledExecutorService activity) {
        this.ahead = 0;
        this.sideways = 0;
        this.lastPressed = Instant.now();


        activity.scheduleAtFixedRate(this::process, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void forwardPressed() {
        ahead = apply(USER_STEP, ahead);
    }

    public void backwardPressed() {
        ahead = apply(-USER_STEP, ahead);
    }

    public void leftPressed() {
        sideways = apply(-USER_STEP, sideways);
    }

    public void rightPressed() {
        sideways = apply(USER_STEP, sideways);
    }

    public ChassisCommand state() {
        return new ChassisCommand(ahead, sideways);
    }

    private synchronized void process() {
        if (Instant.now().toEpochMilli() - lastPressed.toEpochMilli() < DECREMENT_AFTER_INACTIVITY) {
            return;
        }

        if (ahead > 0) {
            ahead = Math.max(0, apply(-DECREMENT_STEP, ahead));
        }
        else if (ahead < 0) {
            ahead = Math.min(0, apply(DECREMENT_STEP, ahead));
        }

        if (sideways > 0) {
            sideways = Math.max(0, apply(-DECREMENT_STEP, sideways));
        }
        else if (sideways < 0) {
            sideways = Math.max(0, apply(DECREMENT_STEP, sideways));
        }
    }

    private int apply(int coef, int value) {
        value += coef;

        value = Math.min(value, UPPER_BOUND);
        value = Math.max(value, LOWER_BOUND);
        lastPressed = Instant.now();
        return value;
    }

    @Override
    public String toString() {
        return "WSADObserver{" +
                "ahead=" + ahead +
                ", sideway=" + sideways +
                '}';
    }
}
