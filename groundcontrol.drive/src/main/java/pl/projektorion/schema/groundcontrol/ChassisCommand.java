package pl.projektorion.schema.groundcontrol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.projektorion.schema.Twist;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisCommand {

    private Twist twist;

    public ChassisCommand() {
    }

    public ChassisCommand(Twist twist) {
        this.twist = twist;
    }

    public Twist getTwist() {
        return twist;
    }

    public void setTwist(Twist twist) {
        this.twist = twist;
    }

    @Override
    public String toString() {
        return "ChassisCommand{" +
                "twist=" + twist +
                '}';
    }

}
