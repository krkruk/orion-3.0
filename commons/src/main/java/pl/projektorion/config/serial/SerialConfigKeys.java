package pl.projektorion.config.serial;

import java.util.Set;

public interface SerialConfigKeys {
    String PREFIX = "serial";
    String PREFIXED = PREFIX + ".";

    String PORT_NAME = "port.name";
    String BAUD_RATE = "baudrate";
    String DEFAULT_TIMEOUT = "timeout";
    String READ_TIMEOUT = "read.timeout";
    String WRITE_TIMEOUT = "write.timeout";
    String POLL_TIMEOUT = "poll.timeout";

    Set<String> MANDATORY_KEYS = Set.of(PORT_NAME, BAUD_RATE, DEFAULT_TIMEOUT);
}
