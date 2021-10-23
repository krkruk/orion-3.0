package pl.projektorion.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Twist {
    private double speed;
    private double angle;

    public Twist() {
    }

    public Twist(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Twist{" +
                "speed=" + speed +
                ", angle=" + angle +
                '}';
    }
}
