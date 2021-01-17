package exceptions;

public class ConfigNotFoundException extends NullPointerException{
    public ConfigNotFoundException(String key){
        super("Config with key \"" + key + "\" doesn't exist" );
    }
}
