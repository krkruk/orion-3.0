package pl.projektorion.backend;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.gamepad.GamepadInput;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.groundcontrol.chassis.ChassisTelemetry;
import pl.projektorion.schema.onboard.chassis.ChassisSerialRxTelemetryMsg;
import pl.projektorion.schema.onboard.chassis.ChassisSerialTxMsgOpenLoop;
import pl.projektorion.serial.OrionDevice;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.serial.OrionSerialDeviceListener;
import pl.projektorion.utils.QueueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GamepadToSerial implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GamepadToSerial.class);

    private final static int IO_THREADS = 1;
    private static final float MAX_PWM = 255.0f;


    private final ExecutorService executor;

    private final OrionDevice<ChassisSerialTxMsgOpenLoop> serial;
    private final GamepadInput<ChassisSerialTxMsgOpenLoop> gamepad;
    private final ObservableQueue<ChassisSerialRxTelemetryMsg> receiverQueue = QueueFactory.createReceiverQueue();
    private final BlockingQueue<ChassisSerialTxMsgOpenLoop> senderQueue = QueueFactory.createSenderQueue();
    private final List<Disposable> disposables = new ArrayList<>();

    public GamepadToSerial(SerialConfig serialConfig, BiConsumer<String, ChassisCommand> uiInput, Consumer<ChassisTelemetry> uiTelemetry) {
        this.executor = Executors.newFixedThreadPool(IO_THREADS);

        this.gamepad = GamepadInput.builder(ChassisSerialTxMsgOpenLoop.class)
                .withController(0)
                .withUpdateInterval(serialConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .withMapper(state -> {
                    final short x = convert(state.leftStickX);
                    final short y = convert(state.leftStickY);
                    return new ChassisSerialTxMsgOpenLoop(x, y);
                })
                .withPeek((controllerName, data) -> uiInput.accept(controllerName, new ChassisCommand(data.getSpeed(), data.getAngle())))
                .withSinkQueue(senderQueue)
                .build();

        this.serial = OrionDevice.builder(ChassisSerialTxMsgOpenLoop.class)
                .withSerialConfig(serialConfig)
                .withCommandSerdes(new OrionJsonSerdes<>(ChassisSerialTxMsgOpenLoop.class))
                .withTelemetryListener(OrionSerialDeviceListener.builder(ChassisSerialRxTelemetryMsg.class)
                        .withSerdes(new OrionJsonSerdes<>(ChassisSerialRxTelemetryMsg.class))
                        .withQueue(receiverQueue)
                        .build())
                .withCommandQueue(senderQueue)
                .build();

        disposables.add(receiverQueue.observe()
                .map(msg -> new ChassisTelemetry(
                        msg.getLeftFrontPwm(),
                        msg.getRightFrontPwm(),
                        msg.getLeftRearPwm(),
                        msg.getRightRearPwm(),
                        msg.getErrorCode(),
                        msg.getErrorDescription()))
                .subscribe(uiTelemetry)
        );
    }

    @Override
    public void run() {
        gamepad.run();
        executor.submit(serial);
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
    }

    private void cleanup() {
        disposables.forEach(Disposable::dispose);
        serial.toggleStop();
        executor.shutdown();
    }

    private static short convert(float value) {
        return (short) (MAX_PWM * value);
    }
}
