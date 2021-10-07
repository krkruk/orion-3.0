package pl.projektorion.config.network.publisher;

import java.util.Set;

public interface PublisherConfigKeys {
    String PREFIX = "publisher";
    String PREFIXED = PREFIX + ".";

    String RECEIVER_ADDRESS = "receiver.address";
    String POLL_TIMEOUT = "poll.timeout";
    String DEFAULT_TIMEOUT = "timeout";

    Set<String> MANDATORY_KEYS = Set.of(RECEIVER_ADDRESS, POLL_TIMEOUT);
}
