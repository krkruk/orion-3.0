package pl.projektorion;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.network.publisher.PublisherConfig;
import pl.projektorion.config.network.publisher.PublisherConfigLoader;
import pl.projektorion.config.network.subscriber.SubscriberConfig;
import pl.projektorion.config.network.subscriber.SubscriberConfigLoader;
import pl.projektorion.gamepad.GamepadController;
import pl.projektorion.gamepad.GamepadInput;
import pl.projektorion.network.Network;
import pl.projektorion.network.publisher.NetworkPublisher;
import pl.projektorion.network.subscriber.NetworkSubscriber;
import pl.projektorion.rx.utils.ObservableQueue;
import pl.projektorion.schema.groundcontrol.chassis.ChassisCommand;
import pl.projektorion.schema.groundcontrol.chassis.ChassisTelemetry;
import pl.projektorion.serial.OrionJsonSerdes;
import pl.projektorion.utils.QueueFactory;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class ControlApp {
    private static final Logger log = LoggerFactory.getLogger(ControlApp.class);

    private JPanel rootPanel;
    private JPanel joystickPanel;
    private JPanel telemetryPanel;
    private JTextField joystickXValue;
    private JTextField joystickYValue;
    private JLabel joystickXLabel;
    private JLabel joystickYLabel;
    private JLabel inputLabel;
    private JLabel joystickName;
    private JTextField joystickNameValue;
    private JLabel telemetryLabel;
    private JLabel leftFrontLabel;
    private JLabel rightFrontLabel;
    private JLabel leftRearLabel;
    private JLabel rightRearLabel;
    private JTextField leftFrontValue;
    private JTextField leftRearValue;
    private JTextField rightFrontValue;
    private JTextField rightRearValue;
    private JLabel errorCodeLabel;
    private JLabel errorDescLabel;
    private JTextField descriptionValue;
    private JTextField errorCodeValue;

    public static void main(String[] args) {
        final CommandLineParser cmd = CommandLineParser.parse(args);
        final PublisherConfig publisherConfig = PublisherConfigLoader.get(cmd);
        final SubscriberConfig subscriberConfig = SubscriberConfigLoader.get(cmd);

        JFrame frame = new JFrame("ControlApp");
        final ControlApp app = new ControlApp();
        frame.setContentPane(app.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final BlockingQueue<ChassisCommand> senderQueue = QueueFactory.createSenderQueue();
        final ObservableQueue<ChassisTelemetry> receiverQueue = QueueFactory.createReceiverQueue();

        GamepadInput.builder(ChassisCommand.class)
                .withUpdateInterval(20, TimeUnit.MILLISECONDS)
                .withMapper(state -> {
                    final float x = state.leftStickX;
                    final float y = state.leftStickY;
                    return new ChassisCommand(x, y);
                })
                .withSinkQueue(senderQueue)
                .withController(0)
                .withPeek(app::setChassisCommandValue)
                .build()
                .run();

        final NetworkPublisher<ChassisCommand> publisher = Network.publisher(ChassisCommand.class)
                .withConfig(publisherConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisCommand.class))
                .withQueue(senderQueue)
                .build();

        final NetworkSubscriber<ChassisTelemetry> subscriber = Network.subscriber(ChassisTelemetry.class)
                .withConfig(subscriberConfig)
                .withSerdes(new OrionJsonSerdes<>(ChassisTelemetry.class))
                .withQueue(receiverQueue)
                .build();

        final Disposable uiTelemetryReceiver = receiverQueue.observe()
                .subscribe(app::setTelemetry);

        executorService.submit(publisher);
        executorService.submit(subscriber);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            publisher.toggleStop();
            subscriber.toggleStop();
            uiTelemetryReceiver.dispose();
            executorService.shutdown();
        }));
    }

    public void setChassisCommandValue(String controllerName, ChassisCommand value) {
        joystickNameValue.setText(controllerName);
        joystickXValue.setText(String.valueOf(value.getXAxis()));
        joystickYValue.setText(String.valueOf(value.getYAxis()));
    }

    public void setTelemetry(ChassisTelemetry telemetry) {
        leftFrontValue.setText(String.valueOf(telemetry.getLeftFrontPwm()));
        rightFrontValue.setText(String.valueOf(telemetry.getRightFrontPwm()));
        leftRearValue.setText(String.valueOf(telemetry.getLeftRearPwm()));
        rightRearValue.setText(String.valueOf(telemetry.getRightRearPwm()));
        errorCodeValue.setText(String.valueOf(telemetry.getErrorCode()));
        descriptionValue.setText(telemetry.getErrorDescription());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        joystickPanel = new JPanel();
        joystickPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(joystickPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, -1), null, null, 0, false));
        joystickXLabel = new JLabel();
        joystickXLabel.setText("X:");
        joystickPanel.add(joystickXLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        joystickYLabel = new JLabel();
        joystickYLabel.setText("Y:");
        joystickPanel.add(joystickYLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        joystickXValue = new JTextField();
        joystickXValue.setEditable(false);
        joystickPanel.add(joystickXValue, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        joystickYValue = new JTextField();
        joystickYValue.setEditable(false);
        joystickPanel.add(joystickYValue, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        joystickName = new JLabel();
        joystickName.setText("Name:");
        joystickPanel.add(joystickName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        joystickNameValue = new JTextField();
        joystickNameValue.setEditable(false);
        joystickPanel.add(joystickNameValue, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        telemetryPanel = new JPanel();
        telemetryPanel.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(telemetryPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftFrontLabel = new JLabel();
        leftFrontLabel.setText("Left Front");
        telemetryPanel.add(leftFrontLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightFrontLabel = new JLabel();
        rightFrontLabel.setText("Right Front");
        telemetryPanel.add(rightFrontLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftRearLabel = new JLabel();
        leftRearLabel.setText("Left Rear");
        telemetryPanel.add(leftRearLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightRearLabel = new JLabel();
        rightRearLabel.setText("Right Rear");
        telemetryPanel.add(rightRearLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftFrontValue = new JTextField();
        leftFrontValue.setEditable(false);
        telemetryPanel.add(leftFrontValue, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        leftRearValue = new JTextField();
        leftRearValue.setEditable(false);
        telemetryPanel.add(leftRearValue, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rightFrontValue = new JTextField();
        rightFrontValue.setEditable(false);
        telemetryPanel.add(rightFrontValue, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rightRearValue = new JTextField();
        rightRearValue.setEditable(false);
        telemetryPanel.add(rightRearValue, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        errorCodeLabel = new JLabel();
        errorCodeLabel.setText("Error code");
        telemetryPanel.add(errorCodeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorDescLabel = new JLabel();
        errorDescLabel.setText("Description");
        telemetryPanel.add(errorDescLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descriptionValue = new JTextField();
        descriptionValue.setEditable(false);
        telemetryPanel.add(descriptionValue, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        errorCodeValue = new JTextField();
        errorCodeValue.setEditable(false);
        telemetryPanel.add(errorCodeValue, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        inputLabel = new JLabel();
        inputLabel.setText("Input");
        rootPanel.add(inputLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        telemetryLabel = new JLabel();
        telemetryLabel.setText("Telemetry");
        rootPanel.add(telemetryLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
