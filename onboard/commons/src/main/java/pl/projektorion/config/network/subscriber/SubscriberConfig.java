package pl.projektorion.config.network.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.ConfigUtils;

import java.util.Properties;

public class SubscriberConfig {
    private static final Logger log = LoggerFactory.getLogger(SubscriberConfig.class);

    private final Properties properties;

    SubscriberConfig(Properties properties) {
        this.properties = properties;
    }

    public String getBindAddress() {
        return this.properties.getProperty(SubscriberConfigKeys.BIND);
    }

    public int getDefaultTimeout() {
        return Integer.parseInt(defaultTimeout());
    }

    public int getReadTimeout() {
        return Integer.parseInt(this.properties.getOrDefault(SubscriberConfigKeys.READ_TIMEOUT, defaultTimeout()).toString());
    }

    public String getTopic() {
        return this.properties.getOrDefault(SubscriberConfigKeys.TOPIC, "").toString();
    }

    private String defaultTimeout() {
        return this.properties.getOrDefault(SubscriberConfigKeys.DEFAULT_TIMEOUT, "50").toString();
    }

    static SubscriberConfig build(Properties props) {
        Properties properties = new Properties();
        properties.putAll(props);
        properties = ConfigUtils.stripPrefix(SubscriberConfigKeys.PREFIXED, properties);

        log.info("Loaded Network Subscriber config = {}", properties);
        return new SubscriberConfig(properties);
    }

    @Override
    public String toString() {
        return "SubscriberConfig{" +
                "bindAddress=" + getBindAddress() +
                ", defaultTimeout=" + getDefaultTimeout() +
                ", readTimeout=" + getReadTimeout() +
                ", topic=" + getTopic() +
                '}';
    }
}
