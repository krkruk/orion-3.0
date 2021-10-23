package pl.projektorion.schema.onboard.chassis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommandMessageOpenLoop {
    @JsonProperty("X")
    private short speed;

    @JsonProperty("Y")
    private short angle;

    public ChassisCommandMessageOpenLoop() {
    }

    public ChassisCommandMessageOpenLoop(short speed, short angle) {
        this.speed = speed;
        this.angle = angle;
    }

    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public short getAngle() {
        return angle;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "ChassisCommandMessageOpenLoop{" +
                "speed (x)=" + speed +
                ", angle (y)=" + angle +
                '}';
    }
}
