package pl.projektorion.schema.onboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisTelemetryMessage {

    private int lPwm;
    private int rPwm;

    public ChassisTelemetryMessage() {
    }

    public ChassisTelemetryMessage(int lPwm, int rPwm) {
        this.lPwm = lPwm;
        this.rPwm = rPwm;
    }

    public int getlPwm() {
        return lPwm;
    }

    public void setlPwm(int lPwm) {
        this.lPwm = lPwm;
    }

    public int getrPwm() {
        return rPwm;
    }

    public void setrPwm(int rPwm) {
        this.rPwm = rPwm;
    }

    @Override
    public String toString() {
        return "ChassisTelemetryMessage{" +
                "lPwm=" + lPwm +
                ", rPwm=" + rPwm +
                '}';
    }
}
