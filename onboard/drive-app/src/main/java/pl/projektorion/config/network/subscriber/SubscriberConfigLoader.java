package pl.projektorion.config.network.subscriber;

import pl.projektorion.config.CommandLineParser;
import pl.projektorion.config.serial.SerialConfig;
import pl.projektorion.config.serial.SerialConfigKeys;

import java.util.Objects;
import java.util.Properties;

public class SubscriberConfigLoader {

    public static SubscriberConfig get(final CommandLineParser cmdArgs) {
        final Properties globalConfig = cmdArgs.getGlobalConfig();
        validateKeys(globalConfig);

        return SubscriberConfig.build(globalConfig);
    }

    private static void validateKeys(final Properties properties) {
        for (String key : SubscriberConfigKeys.MANDATORY_KEYS) {
            final String prefixedKey = SubscriberConfigKeys.PREFIX + key;
            Objects.requireNonNull(properties.getProperty(prefixedKey), "The key is mandatory = " + prefixedKey);
        }
    }
}
