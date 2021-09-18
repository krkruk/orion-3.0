package pl.projektorion.hardware.chassis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommandMessage {

    private int x;
    private int y;

    public ChassisCommandMessage() {
    }

    public ChassisCommandMessage(int x, int y) {
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
        return "ChassisCommandMessage{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
