package pl.projektorion.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CommandLineParser {
    private static final Logger log = LoggerFactory.getLogger(CommandLineParser.class);
    private static final String globalConfig = "-global.config";

    private final Map<String, Properties> properties;

    CommandLineParser(final String[] args) {
        this.properties = new HashMap<>();
        initialize(args);
    }

    public static CommandLineParser parse(final String[] args) {
        return new CommandLineParser(args);
    }

    public Properties getGlobalConfig() {
        assert properties.containsKey(globalConfig) : "Required '" + globalConfig + "' configuration flag";
        final Properties globalProps = new Properties();
        globalProps.putAll(properties.getOrDefault(globalConfig, new Properties()));
        return globalProps;
    }

    public Properties getConfigByPrefix(final String prefix) {
        final Properties config = new Properties();
        config.putAll(properties.getOrDefault(prefix, new Properties()));
        return config;
    }

    private void initialize(final String[] args) {
        if (args.length == 0) {
            log.error("No command line arguments specified. Use {}=your_config_name.properties to specify the config.", globalConfig);
            return;
        }

        final String[] arguments = Arrays.copyOf(args, args.length);
        for (var arg : arguments) {
            final Kv kv = splitArgument(arg);
            if (resourceExists(kv.value)) {
                loadFromFile(kv);
                loadIntoCategories(kv);
            }
            else {
                final String fullKey = kv.key.substring(1); // dropping '-' prefix
                loadFromCliArgument(fullKey, kv.value);
            }
        }
    }

    private Kv splitArgument(final String argument) {
        final String[] split = argument.split("=");
        final String key = split[0];
        final String value = split[1];

        return new Kv(key, value);
    }

    private void loadFromFile(final Kv arg) {
        final Properties props = new Properties();
        loadFromFile(arg.key, arg.value, props);
        log.info("Loaded from file {}={}", arg.key, props);
        updateGlobalConfigMap(arg, props);
    }

    private void loadIntoCategories(final Kv arg) {
        final Properties props = this.properties.getOrDefault(arg.key, new Properties());
        for (var entry : props.entrySet()) {
            loadFromCliArgument(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    private boolean resourceExists(final String resourcePath) {
        final URL relative = CommandLineParser.class.getClassLoader().getResource(resourcePath);
        final URL absolute = CommandLineParser.class.getClassLoader().getResource("/" + resourcePath);
        return relative != null || absolute != null;
    }

    private void loadFromFile(String key, String file, Properties props) {
        InputStream input = CommandLineParser.class.getClassLoader().getResourceAsStream(file);
        try {
            if (input == null) {
                input = Files.newInputStream(Paths.get(file));
            }
            props.load(input);
        } catch (IOException e) {
            log.error("Cannot load {}={}", key, file);
            throw new IllegalStateException("Cannot load property file.", e);
        }
    }

    private void loadFromCliArgument(String key, String value) {
        final String category = key.split("\\.", 2)[0];
        updateGlobalConfigMap(key, value, category);
    }

    private void updateGlobalConfigMap(String key, String value, String category) {
        final Properties configMap = this.properties.getOrDefault(category, new Properties());
        configMap.put(key, value);
        this.properties.put(category, configMap);
    }

    private void updateGlobalConfigMap(Kv arg, Properties props) {
        final Properties config = this.properties.getOrDefault(arg.key, new Properties());
        config.putAll(props);
        this.properties.put(arg.key, config);
    }

    private static class Kv {
        public String key;
        public String value;

        public Kv(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "CommandLineParser{" +
                "properties=" + properties +
                '}';
    }
}
