package pl.projektorion.schema.groundcontrol.chassis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.projektorion.schema.Twist;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommand {

    private float xAxis;
    private float yAxis;

    public ChassisCommand() {
    }

    public ChassisCommand(float xAxis, float yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public float getXAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getYAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
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
