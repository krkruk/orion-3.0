package pl.projektorion.schema.onboard.chassis;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChassisSerialRxTelemetryMsg {
    @JsonProperty("LF")
    private short leftFrontPwm;

    @JsonProperty("RF")
    private short rightFrontPwm;

    @JsonProperty("LB")
    private short leftRearPwm;

    @JsonProperty("RB")
    private short rightRearPwm;

    @JsonProperty("ErrorCode")
    private short errorCode;

    @JsonProperty("ErrorDescription")
    private String errorDescription;

    public ChassisSerialRxTelemetryMsg() {
    }

    public ChassisSerialRxTelemetryMsg(short leftFrontPwm, short rightFrontPwm, short leftRearPwm, short rightRearPwm, short errorCode, String errorDescription) {
        this.leftFrontPwm = leftFrontPwm;
        this.rightFrontPwm = rightFrontPwm;
        this.leftRearPwm = leftRearPwm;
        this.rightRearPwm = rightRearPwm;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public short getLeftFrontPwm() {
        return leftFrontPwm;
    }

    public void setLeftFrontPwm(short leftFrontPwm) {
        this.leftFrontPwm = leftFrontPwm;
    }

    public short getRightFrontPwm() {
        return rightFrontPwm;
    }

    public void setRightFrontPwm(short rightFrontPwm) {
        this.rightFrontPwm = rightFrontPwm;
    }

    public short getLeftRearPwm() {
        return leftRearPwm;
    }

    public void setLeftRearPwm(short leftRearPwm) {
        this.leftRearPwm = leftRearPwm;
    }

    public short getRightRearPwm() {
        return rightRearPwm;
    }

    public void setRightRearPwm(short rightRearPwm) {
        this.rightRearPwm = rightRearPwm;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return "ChassisTelemetryMessage{" +
                "leftFrontPwm=" + leftFrontPwm +
                ", rightFrontPwm=" + rightFrontPwm +
                ", leftRearPwm=" + leftRearPwm +
                ", rightRearPwm=" + rightRearPwm +
                ", errorCode=" + errorCode +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}
