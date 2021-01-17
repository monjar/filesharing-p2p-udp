import config.ConfigLoader;

public class App {

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
    }
}
