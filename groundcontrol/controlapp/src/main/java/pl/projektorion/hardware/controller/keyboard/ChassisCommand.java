package pl.projektorion.hardware.controller.keyboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommand {

    private int x;
    private int y;

    public ChassisCommand() {
    }

    public ChassisCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ChassisCommand{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
