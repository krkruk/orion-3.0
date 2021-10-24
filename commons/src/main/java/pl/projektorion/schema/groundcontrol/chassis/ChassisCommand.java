package pl.projektorion.schema.groundcontrol.chassis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.projektorion.schema.Twist;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommand {

    @JsonProperty("joystickX")
    private double xAxis;

    @JsonProperty("joystickY")
    private double yAxis;

    public ChassisCommand() {
    }

    public ChassisCommand(double xAxis, double yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public double getXAxis() {
        return xAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public double getYAxis() {
        return yAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public String toString() {
        return "ChassisCommand{" +
                "xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }
}
