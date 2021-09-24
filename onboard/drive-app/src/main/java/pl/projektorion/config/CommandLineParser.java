package pl.projektorion.config;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
        final Properties globalProps = new Properties();
        globalProps.putAll(properties.getOrDefault(globalConfig, new Properties()));
        return globalProps;
    }

    private void initialize(final String[] args) {
        if (args.length == 0) {
            log.info("No command line arguments specified.");
            return;
        }

        final String[] arguments = Arrays.copyOf(args, args.length);
        for (var arg : arguments) {
            loadArgument(arg);
        }
    }

    private void loadArgument(final String argument) {
        final String[] split = argument.split("=");
        final String key = split[0];
        final String file = split[1];

        final Properties props = new Properties();
        InputStream input = CommandLineParser.class.getClassLoader().getResourceAsStream(split[1]);
        try {
            if (input == null) {
                input = Files.newInputStream(Paths.get(file));
            }
            props.load(input);
        } catch (IOException e) {
            log.error("Cannot load {}={}", key, file);
            throw new IllegalStateException("Cannot load property file.", e);
        }
        log.info("Properties {}={}", key, props);
        this.properties.put(key, props);
    }
}
