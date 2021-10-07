package pl.projektorion.config.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.projektorion.config.CommandLineParser;

import java.util.Objects;
import java.util.Properties;

public class SerialConfigLoader {
    private static final Logger log = LoggerFactory.getLogger(SerialConfigLoader.class);

    public static SerialConfig get(final CommandLineParser cmdArgs) {
        final Properties config = cmdArgs.getConfigByPrefix(SerialConfigKeys.PREFIX);
        Objects.requireNonNull(config, "Configuration cannot be null");
        validateKeys(config);

        return SerialConfig.build(config);
    }

    private static void validateKeys(final Properties properties) {
        for (String key : SerialConfigKeys.MANDATORY_KEYS) {
            final String prefixedKey = SerialConfigKeys.PREFIXED + key;
            Objects.requireNonNull(properties.getProperty(prefixedKey), "The key is mandatory = " + prefixedKey);
        }
    }
}
