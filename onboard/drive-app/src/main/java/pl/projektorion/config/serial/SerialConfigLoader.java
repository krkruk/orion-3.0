package pl.projektorion.config.serial;

import pl.projektorion.config.CommandLineParser;

import java.util.Objects;
import java.util.Properties;

public class SerialConfigLoader {

    public static SerialConfig get(final CommandLineParser cmdArgs) {
        final Properties globalConfig = cmdArgs.getGlobalConfig();
        validateKeys(globalConfig);

        return SerialConfig.build(globalConfig);
    }

    private static void validateKeys(final Properties properties) {
        for (String key : SerialConfigKeys.MANDATORY_KEYS) {
            final String prefixedKey = SerialConfigKeys.PREFIX + key;
            Objects.requireNonNull(properties.getProperty(prefixedKey), "The key is mandatory = " + prefixedKey);
        }
    }
}
