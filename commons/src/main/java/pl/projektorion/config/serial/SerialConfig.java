package pl.projektorion.config.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.ConfigUtils;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class SerialConfig {
    private static final Logger log = LoggerFactory.getLogger(SerialConfig.class);

    private final Properties properties;

    SerialConfig(Properties properties) {
        this.properties = properties;
    }

    public String getPortName() {
        return this.properties.getProperty(SerialConfigKeys.PORT_NAME);
    }

    public int getBaudRate() {
        return Integer.parseInt(this.properties.getOrDefault(SerialConfigKeys.BAUD_RATE, "9600").toString());
    }

    public int getDefaultTimeout() {
        return Integer.parseInt(defaultTimeout());
    }

    public int getReadTimeout() {
        return Integer.parseInt(this.properties.getOrDefault(SerialConfigKeys.READ_TIMEOUT, defaultTimeout()).toString());
    }

    public int getWriteTimeout() {
        return Integer.parseInt(this.properties.getOrDefault(SerialConfigKeys.WRITE_TIMEOUT, defaultTimeout()).toString());
    }

    public long getPollTimeout() {
        return Long.parseLong(this.properties.getOrDefault(SerialConfigKeys.POLL_TIMEOUT, defaultTimeout()).toString());
    }

    private String defaultTimeout() {
        return this.properties.getOrDefault(SerialConfigKeys.DEFAULT_TIMEOUT, "50").toString();
    }

    static SerialConfig build(Properties props) {
        Properties properties = new Properties();
        properties.putAll(props);
        properties = ConfigUtils.stripPrefix(SerialConfigKeys.PREFIXED, properties);

        log.info("Loaded serial config = {}", properties);
        return new SerialConfig(properties);
    }

    @Override
    public String toString() {
        return "SerialConfig{" +
                "portName=" + getPortName() +
                ", baudRate=" + getBaudRate() +
                ", defaultTimeout=" + getDefaultTimeout() +
                ", readTimeout=" + getReadTimeout() +
                ", writeTimeout=" + getWriteTimeout() +
                ", pollTimeout=" + getPollTimeout() +
                '}';
    }
}
