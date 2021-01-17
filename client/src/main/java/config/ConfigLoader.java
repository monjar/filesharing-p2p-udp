package config;

import exceptions.ConfigFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ConfigLoader {
    private String filename;

    public ConfigLoader(String filename) {
        this.filename = filename;
    }

    public String load() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            Properties prop = new Properties();
            if (input == null)
                throw new ConfigFileNotFoundException();
            prop.load(input);
            prop.forEach((key, value) -> Configs.put((String) key, (String) value));
            return Configs.getConfigString();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return "File not found";
        }
    }


}
