package pl.projektorion.gamepad;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class GamepadController {
    private static volatile GamepadController instance;

    private final ControllerManager manager;

    private GamepadController(ControllerManager manager) {
        this.manager = manager;
        this.manager.initSDLGamepad();
        Runtime.getRuntime().addShutdownHook(new Thread(manager::quitSDLGamepad));
    }

    public ControllerManager getManager() {
        return manager;
    }

    public ControllerState getState(int index) {
        return getManager().getState(index);
    }

    public static GamepadController getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (GamepadController.class) {
            if (instance == null) {
                instance = new GamepadController(new ControllerManager());
            }
        }
        return instance;
    }
}
