package pl.projektorion.config.network.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.ConfigUtils;

import java.util.Properties;

public class PublisherConfig {
    private static final Logger log = LoggerFactory.getLogger(PublisherConfig.class);

    private final Properties properties;

    PublisherConfig(Properties properties) {
        this.properties = properties;
    }

    public String getReceiverAddress() {
        return this.properties.getProperty(PublisherConfigKeys.RECEIVER_ADDRESS);
    }

    public int getDefaultTimeout() {
        return Integer.parseInt(defaultTimeout());
    }

    public int getPollTimeout() {
        return Integer.parseInt(this.properties.getOrDefault(PublisherConfigKeys.POLL_TIMEOUT, defaultTimeout()).toString());
    }

    private String defaultTimeout() {
        return this.properties.getOrDefault(PublisherConfigKeys.DEFAULT_TIMEOUT, "50").toString();
    }

    static PublisherConfig build(Properties props) {
        Properties properties = new Properties();
        properties.putAll(props);
        properties = ConfigUtils.stripPrefix(PublisherConfigKeys.PREFIXED, properties);

        log.info("Loaded Network Publisher config = {}", properties);
        return new PublisherConfig(properties);
    }

    @Override
    public String toString() {
        return "PublisherConfig{" +
                "receiver.address=" + getReceiverAddress() +
                ", defaultTimeout=" + getDefaultTimeout() +
                ", readTimeout=" + getPollTimeout() +
                '}';
    }
}
