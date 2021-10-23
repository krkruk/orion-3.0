package pl.projektorion.config.network.subscriber;

import java.util.Set;

public interface SubscriberConfigKeys {
    String PREFIX = "subscriber";
    String PREFIXED = PREFIX + ".";

    String BIND = "bind";
    String READ_TIMEOUT = "read.timeout";
    String TOPIC = "topic";
    String DEFAULT_TIMEOUT = "timeout";

    Set<String> MANDATORY_KEYS = Set.of(BIND, READ_TIMEOUT);
}
