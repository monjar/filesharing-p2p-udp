import config.ConfigLoader;
import config.Configs;
import exceptions.ConfigFileNotFoundException;
import exceptions.ConfigNotFoundException;
import org.classpath.icedtea.Config;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import util.DataHelpers;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UtilTest {
    @Test
    public void parseData() {
        String s1 = " THis is a string for testing parse. \n still testing...";
        assertEquals(s1, DataHelpers.parseBytes(s1.getBytes()));
    }

    @Test
    public void config() {
        ConfigLoader c = new ConfigLoader("config.properties");
        c.load();
        assertEquals("test", Configs.getStr("test.prop"));
    }
    @Test
    public void configFileException() {
        assertThrows(ConfigFileNotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                ConfigLoader c = new ConfigLoader("NOTFOUNDFILE");
                c.load();
            }
        });
    }

    @Test
    public void configKeyException() {

        assertThrows(ConfigNotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                ConfigLoader c = new ConfigLoader("config.properties");
                c.load();
                Configs.getStr("NOTFOUNDCONGIG");
            }
        });
    }
}
