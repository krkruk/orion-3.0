package pl.projektorion.hardware.controller.keyboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WSADController implements KeyListener {
    private static final Logger log = LoggerFactory.getLogger(WSADController.class);

    private final WSADObserver observer;

    public WSADController(WSADObserver observer) {
        this.observer = observer;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                observer.forwardPressed(); break;
            case KeyEvent.VK_S:
                observer.backwardPressed(); break;
            case KeyEvent.VK_A:
                observer.leftPressed(); break;
            case KeyEvent.VK_D:
                observer.rightPressed(); break;
            default:
                //ignore
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
