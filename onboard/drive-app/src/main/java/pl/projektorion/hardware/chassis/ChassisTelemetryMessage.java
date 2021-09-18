package pl.projektorion.hardware.chassis;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisTelemetryMessage {

    private int resX;
    private int resY;

    public ChassisTelemetryMessage() {
    }

    public ChassisTelemetryMessage(int resX, int resY) {
        this.resX = resX;
        this.resY = resY;
    }

    public int getResX() {
        return resX;
    }

    public void setResX(int resX) {
        this.resX = resX;
    }

    public int getResY() {
        return resY;
    }

    public void setResY(int resY) {
        this.resY = resY;
    }

    @Override
    public String toString() {
        return "ChassisTelemetryMessage{" +
                "resX=" + resX +
                ", resY=" + resY +
                '}';
    }
}
