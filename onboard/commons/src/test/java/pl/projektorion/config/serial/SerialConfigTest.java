package pl.projektorion.config.serial;

import org.junit.jupiter.api.Test;
import pl.projektorion.config.CommandLineParser;

import static org.junit.jupiter.api.Assertions.*;

class SerialConfigTest {

    @Test
    void testGetPortNameFromFile() {
        final CommandLineParser cmd = CommandLineParser.parse(new String[]{"-global.config=serial.config.test.properties"});
        final SerialConfig serialConfig = SerialConfigLoader.get(cmd);

        assertEquals("my_serial_port", serialConfig.getPortName());
    }

    @Test
    void testGetPortNameFromFileOverrideDefault() {
        final String portName = "my_new_serial_port_name";
        final CommandLineParser cmd = CommandLineParser.parse(new String[]{"-global.config=serial.config.test.properties", "-serial.port.name=" + portName});
        final SerialConfig serialConfig = SerialConfigLoader.get(cmd);
        assertEquals(portName, serialConfig.getPortName());
    }
}