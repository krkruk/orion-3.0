package pl.projektorion.config;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigUtils {
    public static Properties stripPrefix(final String prefix, final Properties properties) {
        final List<String> prefixedProperties = properties.keySet().stream()
                .map(Objects::toString)
                .filter(k -> k.startsWith(prefix))
                .collect(Collectors.toList());

        final Properties result = new Properties();
        for (String p : prefixedProperties) {
            final String value = properties.getProperty(p);
            final String key = p.replaceFirst(prefix, "");
            result.put(key, value);
        }

        return result;
    }
}
