package config;

import exceptions.ConfigNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Configs {

    private static final Map<String, String> configMap = new HashMap<>();

    public static String get(String key) {
        if (!configMap.containsKey(key))
            throw new ConfigNotFoundException(key);
        else
            return configMap.get(key);
    }

    public static int getInt(String key) {
        return Integer.valueOf(get(key));
    }

    public static String getStr(String key) {
        return get(key);
    }

    protected static void put(String key, String value) {
        configMap.put(key, value);
    }

    protected static String getConfigString() {
        final StringBuilder rValue = new StringBuilder();
        configMap.forEach((key, value) -> {
            configMap.put(key, value);
            rValue.append("Key : ").append(key).append(", Value : ").append(value).append("\n");
        });
        return rValue.toString();
    }
}
