package pl.projektorion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.hardware.controller.keyboard.WSADController;
import pl.projektorion.hardware.controller.keyboard.WSADObserver;
import pl.projektorion.network.NetworkPublisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControlApp {
    private static final Logger log = LoggerFactory.getLogger(ControlApp.class);

    public ControlApp() {
        helloWorldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ControlApp");
        final ControlApp app = new ControlApp();
        frame.setContentPane(app.root);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);

        frame.setFocusable(true);

        final ScheduledExecutorService activity = Executors.newSingleThreadScheduledExecutor();
        WSADObserver observer = new WSADObserver(activity);
        frame.addKeyListener(new WSADController(observer));

        final ExecutorService networkThread = Executors.newSingleThreadExecutor();
        final NetworkPublisher networkPublisher = new NetworkPublisher(observer);
        networkThread.submit(networkPublisher);

        Runtime.getRuntime().addShutdownHook(new Thread(activity::shutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkPublisher.toggleStopped();
            networkThread.shutdown();
        }));
    }

    private JPanel root;
    private JButton helloWorldButton;

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
        root = new JPanel();
        root.setLayout(new GridBagLayout());
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        root.add(spacer2, gbc);
        helloWorldButton = new JButton();
        helloWorldButton.setText("Quit");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root.add(helloWorldButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
