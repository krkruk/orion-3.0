package pl.projektorion.config.network.publisher;

import pl.projektorion.config.CommandLineParser;

import java.util.Objects;
import java.util.Properties;

public class PublisherConfigLoader {

    public static PublisherConfig get(final CommandLineParser cmdArgs) {
        final Properties globalConfig = cmdArgs.getConfigByPrefix(PublisherConfigKeys.PREFIX);
        validateKeys(globalConfig);

        return PublisherConfig.build(globalConfig);
    }

    private static void validateKeys(final Properties properties) {
        for (String key : PublisherConfigKeys.MANDATORY_KEYS) {
            final String prefixedKey = PublisherConfigKeys.PREFIXED + key;
            Objects.requireNonNull(properties.getProperty(prefixedKey), "The key is mandatory = " + prefixedKey);
        }
    }
}
